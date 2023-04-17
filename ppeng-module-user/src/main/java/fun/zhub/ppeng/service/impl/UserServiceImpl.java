package fun.zhub.ppeng.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.dto.UserInfoDTO;
import fun.zhub.ppeng.dto.login.LoginFormDTO;
import fun.zhub.ppeng.dto.register.RegisterDTO;
import fun.zhub.ppeng.dto.update.UpdateUserEmailDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserMapper;
import fun.zhub.ppeng.service.UserInfoService;
import fun.zhub.ppeng.service.UserService;
import fun.zhub.ppeng.service.WeChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RabbitConstants.*;
import static com.zhub.ppeng.constant.RedisConstants.*;
import static com.zhub.ppeng.constant.RoleConstants.DEFAULT_NICK_NAME_PREFIX;
import static com.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static com.zhub.ppeng.constant.SystemConstants.PPENG_URL;
import static fun.zhub.ppeng.contants.WeChatApiContants.*;

/**
 * <p>
 * UserServiceImpl，实现UserService interface
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Resource
    private UserMapper userMapper;


    @Resource
    private Snowflake snowflake;

    @Resource
    private RSA rsa;


    @Resource
    private WeChatService weChatService;

    @Resource
    private UserInfoService userInfoService;


    /**
     * 实现用户注册功能
     *
     * @param registerDTO registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();
        String password = registerDTO.getPassword();

        try {
            password = rsa.decryptStr(password, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "密码未加密");
        }

        // 检查密码强度
        boolean matches = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$");
        if (BooleanUtil.isFalse(matches)) {
            throw new BusinessException(ResponseStatus.FAIL, "密码太弱");
        }

        // 验证验证码和邮箱
        if (!verifyEmail(email, code, REGISTER_CODE_KEY)) {
            throw new BusinessException(ResponseStatus.FAIL, "验证码错误");
        }

        // 查询email是否已经注册
        User one = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));


        if (BeanUtil.isNotEmpty(one)) {
            throw new BusinessException(ResponseStatus.FAIL, "用户已经注册");
        }


        Long id = snowflake.nextId();

        String newPassword = SmUtil.sm3(id + password);

        User user = new User();
        user.setId(id);
        user.setPassword(newPassword);
        user.setEmail(email);

        createUser(user);
    }


    /**
     * 实现通过密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return id
     */
    @Override
    public User loginByPassword(LoginFormDTO loginFormDTO) {
        String email = loginFormDTO.getEmail();
        String password;
        try {
            password = rsa.decryptStr(loginFormDTO.getPassword(), KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "密码未加密");
        }


        // 根据邮箱查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));

        if (user == null) {
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        // 验证密码
        password = SmUtil.sm3(user.getId() + password);
        if (!StrUtil.equals(password, user.getPassword())) {
            throw new BusinessException(ResponseStatus.FAIL, "密码错误");
        }


        return user;
    }

    /**
     * 实现微信登录功能
     *
     * @param code code
     * @return user
     */
    @Override
    public Map<String, Object> loginByWeChat(String code) {
        Map<String, Object> map = new HashMap<>();
        // 使用OpenFeign调用微信登录接口，获取该用户的唯一openId
        String json = weChatService.loginByWeChat(APP_ID, SECRET, code, GRANT_TYPE);

        JSONObject jsonObject = JSONUtil.parseObj(json);

        String openId = (String) jsonObject.get("openid");

        if (StrUtil.isEmpty(openId)) {
            log.info(json);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, jsonObject.get("errmsg", String.class));
        }

        // 查看该用户是否已经注册
        User one = userMapper.selectOne(new QueryWrapper<User>().eq("open_id", openId));

        // 如果存在，则直接返回
        if (BeanUtil.isNotEmpty(one)) {
            map.put("user", one);
            map.put("isFirst", false);
            return map;
        }

        // 如果不不存在，则创建
        Long id = snowflake.nextId();

        User user = new User();
        user.setId(id);
        user.setOpenId(openId);

        createUser(user);
        map.put("user", user);
        map.put("isFirst", true);

        return map;
    }


    /**
     * 实现验证登录信息之后的操作
     *
     * @param user user
     * @return authentication
     */
    @Override
    public String afterLogin(User user) {
        Long userId = user.getId();
        // 登录
        StpUtil.login(userId);

        // 先完成一遍查询用户操作，写入本地缓存,和redis缓存
        getUserInfoById(userId);

        // 将用户角色信息插入redis
        stringRedisTemplate.opsForSet().add(USER_ROLE + userId, user.getRole());

        /*
         * 异步加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_USER_CACHE, userId);

        return StpUtil.getTokenValue();
    }


    /**
     * 实现根据id获取用户信息
     *
     * @param id id
     * @return user
     */
    @Override
    @Cacheable(cacheNames = "userInfo", key = "#id", sync = true)
    public UserInfoDTO getUserInfoById(Long id) {
        String key = USER_INFO + id;
        // 先查询redis
        String json = stringRedisTemplate.opsForValue().get(key);


        // redis中存在 则返回
        if (StrUtil.isNotEmpty(json)) {
            return JSONUtil.toBean(json, UserInfoDTO.class);
        }

        // redis中不存在，则查询数据库
        // 查询user表
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        // 查询UserInfo表

        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", id));
        if (BeanUtil.isEmpty(userInfo)) {
            // 不存在，则创建
            userInfo = userInfoService.createUserInfoById(id);
        }

        UserInfoDTO userInfoDTO = BeanUtil.copyProperties(user, UserInfoDTO.class);
        BeanUtil.copyProperties(userInfo, userInfoDTO);

        log.info(userInfoDTO.toString());

        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userInfoDTO), USER_INFO_TTL, TimeUnit.HOURS);

        return userInfoDTO;
    }


    /**
     * 实现更新用户密码
     *
     * @param userPasswordDTO userPasswordDTO
     */
    @Override
    public void updatePassword(UpdateUserPasswordDTO userPasswordDTO) {
        String email = userPasswordDTO.getEmail();
        Long id = userPasswordDTO.getUserId();

        if (!verifyEmail(email, userPasswordDTO.getVerifyCode(), UPDATE_CODE_KEY)) {
            // 验证码错误
            throw new BusinessException(ResponseStatus.FAIL, "验证码错误");
        }


        // 一致，根据id查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));

        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }

        // 验证email是否一致
        if (!StrUtil.equals(user.getEmail(), email)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "该邮箱不属于该账号");
        }


        // 密码解密/加密
        String newPassword;
        try {
            newPassword = rsa.decryptStr(userPasswordDTO.getNewPassword(), KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.FAIL, "密码未加密");
        }

        // 检查密码强度
        boolean matches = newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+-=;:'\",.<>/?\\[\\]{}|`~]{8,20}?$");
        if (!matches) {
            throw new BusinessException(ResponseStatus.FAIL, "密码太弱");
        }


        newPassword = SmUtil.sm3(userPasswordDTO.getUserId() + newPassword);

        if (StrUtil.equals(user.getPassword(), newPassword)) {
            // 如果新旧密码一致，则返回错误结果
            throw new BusinessException(ResponseStatus.FAIL, "新旧密码不能一致");
        }


        boolean b = update(new UpdateWrapper<User>()
                .set("password", newPassword)
                .set("update_time", LocalDateTime.now())
                .eq("id", id));


        if (!b) {
            log.error("更新用户{}失败", userPasswordDTO.getUserId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "内部错误-更新失败");
        }

    }


    /**
     * 实现更新用户邮箱
     *
     * @param userEmailDTO userEmailDTO
     */
    @Override
    @CacheEvict(cacheNames = "userInfo", key = "#userEmailDTO.userId")
    public void updateEmail(UpdateUserEmailDTO userEmailDTO) {
        Long id = userEmailDTO.getUserId();
        String oldEmail = userEmailDTO.getOldEmail();
        String newEmail = userEmailDTO.getNewEmail();

        // 验证新旧邮箱的验证码
        if (!verifyEmail(oldEmail, userEmailDTO.getOldCode(), UPDATE_CODE_KEY)) {
            throw new BusinessException(ResponseStatus.FAIL, oldEmail + "-验证码错误");
        }
        if (!verifyEmail(newEmail, userEmailDTO.getNewCode(), UPDATE_CODE_KEY)) {
            throw new BusinessException(ResponseStatus.FAIL, newEmail + "-验证码错误");
        }

        // 不允许新旧邮箱相同
        if (StrUtil.equals(oldEmail, newEmail)) {
            throw new BusinessException(ResponseStatus.FAIL, oldEmail + "新旧邮箱不允许一致");
        }


        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));

        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }

        // 验证邮箱是否一致
        if (!StrUtil.equals(user.getEmail(), oldEmail)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "该邮箱不属于该账号");
        }

        boolean b = update(new UpdateWrapper<User>()
                .set("email", newEmail)
                .set("update_time", LocalDateTime.now())
                .eq("id", id));

        if (!b) {
            log.error("更新用户{}失败", user.getId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "内部错误-更新失败");
        }


    }

    /**
     * 实现更新用户昵称和头像操作
     *
     * @param userId   userId
     * @param nickName 昵称
     * @param icon     头像url
     */
    @Override
    @CacheEvict(cacheNames = "userInfo", key = "#userId")
    public void updateNickNameAndIcon(Long userId, String nickName, String icon) {
        // 根据Id获取当前用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));


        // 用于判断是否需要更新
        boolean b = false;

        if (StrUtil.isNotEmpty(nickName)) {
            b = true;
            user.setNickName(nickName);
        }

        if (StrUtil.isNotEmpty(icon)) {
            if (StrUtil.isNotEmpty(user.getIcon())) {
                // 异步删除旧的icon
                String oldIcon = user.getIcon().replace(PPENG_URL, "");
                rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_FILE_DELETE, oldIcon);
            }
            b = true;
            user.setIcon(icon);
        }

        if (b) {
            user.setUpdateTime(LocalDateTime.now());
            int i = userMapper.updateById(user);
            if (i == 0) {
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新失败");
            }
            log.info("更新用户信息{}成功,更新时间：{}", user.getId(), user.getUpdateTime());
        }


    }


    /**
     * 实现根据id删除用户
     *
     * @param id id
     */
    @Override
    @CacheEvict(cacheNames = "userInfo", key = "#id")
    public void deleteUserById(Long id) {
        // 删除当前用户的基本信息
        int c = userMapper.deleteById(id);

        if (c == 0) {
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        // 删除用户的具体信息
        userInfoService.deleteUserInfoById(id);


    }


    /**
     * 实现根据user创建新用户
     *
     * @param user user
     * @return UserInfoDTO
     */
    @Override
    public UserInfoDTO createUser(User user) {
        user.setNickName(DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setRole(ROLE_USER);
        user.setCreateTime(LocalDateTime.now());
        user.setIsDeleted((byte) 0);


        int i = userMapper.insert(user);
        if (i == 0) {
            log.error("创建用户{}失败", user.getId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "新建用户失败");
        }

        log.info("创建用户{}成功,创建时间：{}", user.getId(), user.getCreateTime());


        // 创建用户的具体信息
        UserInfo userInfo = userInfoService.createUserInfoById(user.getId());

        UserInfoDTO userInfoDTO = BeanUtil.copyProperties(user, UserInfoDTO.class);
        BeanUtil.copyProperties(userInfo, userInfoDTO);

        return userInfoDTO;
    }


    /**
     * 实现验证 验证码和email是否匹配
     *
     * @param email email
     * @param code  验证码
     * @param key   redis存储对应验证码的前缀
     * @return 是否匹配
     */
    @Override
    public Boolean verifyEmail(String email, String code, String key) {

        String cacheCode = stringRedisTemplate.opsForValue().get(key + email);

        // 验证验证码
        // 不一致，抛出异常
        return StrUtil.equals(code, cacheCode);
    }

}
