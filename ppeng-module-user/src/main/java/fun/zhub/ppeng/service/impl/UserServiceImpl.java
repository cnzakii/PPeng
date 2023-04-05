package fun.zhub.ppeng.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import static com.zhub.ppeng.constant.RabbitConstants.ROUTING_USER_CACHE;
import static com.zhub.ppeng.constant.RabbitConstants.USER_EXCHANGE_NAME;
import static com.zhub.ppeng.constant.RedisConstants.*;
import static com.zhub.ppeng.constant.RoleConstants.DEFAULT_NICK_NAME_PREFIX;
import static com.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static com.zhub.ppeng.constant.SaTokenConstants.SESSION_USER;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.dto.UserDTO;
import fun.zhub.ppeng.dto.login.PasswordLoginFormDTO;
import fun.zhub.ppeng.dto.login.VerifyCodeLoginFormDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPhoneDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.mapper.UserMapper;
import fun.zhub.ppeng.service.UserInfoService;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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
    private UserInfoService userInfoService;


    /**
     * 实现通过验证码登录
     *
     * @param loginFormDTO 用户验证码登录结构体
     * @return UserDTO
     */
    @Override
    public User loginByVerifyCode(VerifyCodeLoginFormDTO loginFormDTO) {
        String phone = loginFormDTO.getPhone();

        if (!verifyPhone(phone, loginFormDTO.getVerifyCode(), LOGIN_CODE_KEY)) {
            // 验证码错误
            throw new BusinessException(ResponseStatus.FAIL, "验证码错误");
        }

        // 一致，根据手机号查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));


        if (user == null) {
            // 不存在则新建用户
            Long userId = snowflake.nextId();
            user = createUser(userId, phone);
        }

        return user;
    }


    /**
     * 实现通过密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return User
     */
    @Override
    public User loginByPassword(PasswordLoginFormDTO loginFormDTO) {
        String phone = loginFormDTO.getPhone();
        String password;
        try {
            password = rsa.decryptStr(loginFormDTO.getPassword(), KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.FAIL, "密码未加密");
        }


        // 根据手机号查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));

        if (user == null) {
            throw new BusinessException(ResponseStatus.FAIL, "用户尚未注册");
        }

        // 验证密码
        password = SmUtil.sm3(user.getId() + password);
        if (!StrUtil.equals(password, user.getPassword())) {
            throw new BusinessException(ResponseStatus.FAIL, "密码错误");
        }


        return user;
    }


    /**
     * 实现验证登录信息之后的操作
     *
     * @param user 用户
     * @return authentication
     */
    @Override
    public String afterLogin(User user) {

        StpUtil.login(user.getId());

        SaSession session = StpUtil.getSession();

        // 查询session是否存在user角色等信息
        if (session.get(SESSION_USER) == null) {
            // 将用户基本信息和角色信息保存进session中
            session.set(SESSION_USER, user);
            // 将user的用户信息存入redis
            stringRedisTemplate.opsForValue().set(USER_ROLE + user.getId(), user.getRole(), USER_ROLE_TTL, TimeUnit.DAYS);
        }


        /*
         * 异步加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */
        rabbitTemplate.convertAndSend(USER_EXCHANGE_NAME, ROUTING_USER_CACHE, user.getId());


        return StpUtil.getTokenValue();
    }


    /**
     * 实现根据id获取用户基本信息
     *
     * @param id id
     * @return userDTO
     */
    @Override
    public UserDTO getUserInfoById(Long id) {
        String key = USER_BASE_INFO + id;
        // 先查询redis
        String baseInfo = stringRedisTemplate.opsForValue().get(key);

        // 有则直接返回并刷新过期时间
        if (StrUtil.isNotEmpty(baseInfo)) {
            stringRedisTemplate.expire(key, USER_BASE_INFO_TTL, TimeUnit.MINUTES);
            return JSONUtil.toBean(baseInfo, UserDTO.class);
        }

        // 没有则查询数据库并写入redis
        User user = query().eq("id", id).one();
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 手机号脱敏
        String mobilePhone = DesensitizedUtil.mobilePhone(userDTO.getPhone());
        userDTO.setPhone(mobilePhone);

        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userDTO), USER_BASE_INFO_TTL, TimeUnit.MINUTES);

        return userDTO;
    }


    /**
     * 实现更新用户密码
     *
     * @param userPasswordDTO userPasswordDTO
     */
    @Override
    public void updatePassword(UpdateUserPasswordDTO userPasswordDTO) {
        String phone = userPasswordDTO.getPhone();

        if (!verifyPhone(phone, userPasswordDTO.getVerifyCode(), LOGIN_CODE_KEY)) {
            // 验证码错误
            throw new BusinessException(ResponseStatus.FAIL, "验证码错误");
        }


        // 一致，根据手机号查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));

        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }


        // 密码解密/加密
        String newPassword;
        try {
            newPassword = rsa.decryptStr(userPasswordDTO.getNewPassword(), KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.FAIL, "密码未加密");
        }

        newPassword = SmUtil.sm3(userPasswordDTO.getUserId() + newPassword);

        if (StrUtil.isNotEmpty(user.getPassword())) {
            // 如果新旧密码一致，则返回错误结果
            throw new BusinessException(ResponseStatus.FAIL, "新旧密码不能一致");
        }

        user.setPassword(newPassword);

        boolean b = updateById(user);

        if (!b) {
            log.error("更新用户{}失败", userPasswordDTO.getUserId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "内部错误-更新失败");
        }

    }

    /**
     * 实现更新用户手机号
     *
     * @param userPhoneDTO userPhoneDTO
     */
    @Override
    public void updatePhone(UpdateUserPhoneDTO userPhoneDTO) {
        String oldPhone = userPhoneDTO.getOldPhone();
        String newPhone = userPhoneDTO.getNewPhone();

        // 验证新旧手机号的验证码
        if (!verifyPhone(oldPhone, userPhoneDTO.getOldCode(), USER_UPDATE_CODE_KEY)) {
            throw new BusinessException(ResponseStatus.FAIL, oldPhone + "-验证码错误");
        }
        if (!verifyPhone(newPhone, userPhoneDTO.getNewCode(), USER_UPDATE_CODE_KEY)) {
            throw new BusinessException(ResponseStatus.FAIL, oldPhone + "-验证码错误");
        }

        // 不允许新旧手机号相同
        if (StrUtil.equals(oldPhone, newPhone)) {
            throw new BusinessException(ResponseStatus.FAIL, oldPhone + "新旧手机号不允许一致");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", oldPhone));

        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }

        user.setPhone(newPhone);

        boolean b = updateById(user);

        if (!b) {
            log.error("更新用户{}失败", user.getId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "内部错误-更新失败");
        }


    }


    /**
     * 实现根据用户id更新昵称
     *
     * @param id       id
     * @param nickName 昵称
     */
    @Override
    public void updateNickNameById(Long id, String nickName) {
        boolean b = update(new UpdateWrapper<User>().set("nick_name", nickName).eq("id", id));
        if (!b) {
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        /*
         * TODO 使用MQ将昵称传个第三方审核接口进行审核。
         */

    }

    /**
     * 实现根据id更新头像
     *
     * @param id   id
     * @param icon 头像
     * @return 路径
     */
    @Override
    public String updateIconById(Long id, MultipartFile icon) {
        /*
         * TODO 保存文件到OSS或者本地,得到存储路径
         */
        String path = "";

        boolean b = update(new UpdateWrapper<User>().set("icon", icon).eq("id", id));

        if (!b) {
            /*
             * TODO 删除文件
             */
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }


        return path;
    }

    /**
     * 实现根据id删除用户
     *
     * @param id id
     */
    @Override
    public void deleteUserById(Long id) {
        // 删除当前用户的基本信息
        boolean b = removeById(id);

        if (!b) {
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        // 删除用户的具体信息
        userInfoService.deleteUserInfo(null);


    }


    /**
     * 实现根据id和手机号创建新用户
     *
     * @param id    id
     * @param phone 用户手机号
     * @return user
     */
    @Override
    public User createUser(Long id, String phone) {
        User user = new User();
        user.setId(id);
        user.setPhone(phone);
        user.setNickName(DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setRole(ROLE_USER);
        user.setCreateTime(LocalDateTime.now());


        int i = userMapper.insert(user);
        if (i == 0) {
            log.error("创建用户{}失败", user.getId());
            throw new BusinessException(ResponseStatus.FAIL, "新建用户失败");
        }

        log.info("创建用户{}成功,创建时间：{}", user.getId(), user.getCreateTime());
        return user;
    }


    /**
     * 实现验证 验证码和手机号是否匹配
     *
     * @param phone phone
     * @param code  验证码
     * @param key   redis存储对应验证码的前缀
     * @return 是否匹配
     */
    @Override
    public Boolean verifyPhone(String phone, String code, String key) {

        String cacheCode = stringRedisTemplate.opsForValue().get(key + phone);

//        // 验证验证码
//        if (!StrUtil.equals(code, cacheCode)) {
//            // 不一致，抛出异常
//           return false
//        }

        return true;
    }

}
