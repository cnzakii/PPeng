package fun.zhub.ppeng.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.feign.FileService;
import fun.zhub.ppeng.feign.WeChatService;
import fun.zhub.ppeng.mapper.UserMapper;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RabbitConstants.*;
import static fun.zhub.ppeng.constant.RedisConstants.*;
import static fun.zhub.ppeng.constant.RoleConstants.DEFAULT_NICK_NAME_PREFIX;
import static fun.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static fun.zhub.ppeng.contant.WeChatApiContants.*;

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
    private WeChatService weChatService;

    @Resource
    private FileService fileService;


    /**
     * 实现用户注册功能
     *
     * @param email    邮箱
     * @param password 密码
     */
    @Override
    public void register(String email, String password) {

        // 检查密码强度
        boolean matches = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+-=;:'\",.<>/?\\[\\]{}|`~]{8,20}$");
        if (BooleanUtil.isFalse(matches)) {
            throw new BusinessException(ResponseStatus.FAIL, "密码太弱");
        }

        // 查询email是否已经注册，包括已经删除的
        Optional.ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email).in(User::getIsDeleted, 0, 1)))
                .ifPresent(u -> {
                    throw new BusinessException(ResponseStatus.FAIL, "用户已经注册");
                });


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
     * @param email    邮件
     * @param password 密码
     * @return user
     */
    @Override
    public User loginByPassword(String email, String password) {

        // 根据邮箱查询用户
        User user = Optional
                .ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email)))
                .orElseThrow(() -> new BusinessException(ResponseStatus.FAIL, "用户不存在"));


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
    public User loginByWeChat(String code) {
        // 使用OpenFeign调用微信登录接口，获取该用户的唯一openId
        String json = weChatService.loginByWeChat(APP_ID, SECRET, code, GRANT_TYPE);

        // 获取openId
        String openId = Optional.ofNullable(JSONUtil.parseObj(json).getStr("openid"))
                .orElseThrow(() -> {
                    JSONObject jsonObject = JSONUtil.parseObj(json);
                    log.info(json);
                    return new BusinessException(ResponseStatus.HTTP_STATUS_400, jsonObject.get("errmsg", String.class));
                });

        // 获取user对象，没有则创建
        User user;
        user = Optional.ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId)))
                .orElseGet(() -> {
                    Long id = snowflake.nextId();
                    User newUser = new User();
                    newUser.setId(id);
                    newUser.setOpenId(openId);
                    return createUser(newUser);
                });

        return user;
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
        stringRedisTemplate.expire(USER_ROLE + userId, USER_INFO_TTL, TimeUnit.HOURS);

        /*
         * 异步加载用户其他信息：具体关注，具体粉丝，具体发布的笔记等
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
    @Cacheable(cacheNames = "userInfo", key = "#id")
    public User getUserInfoById(Long id) {
        String key = USER_INFO + id;
        User one;

        // 先查询redis，
        one = Optional.ofNullable(stringRedisTemplate.opsForValue().get(key))
                .map(json -> JSONUtil.toBean(json, User.class))
                .orElseGet(() -> {
                    // redis不存在则查询数据库，数据库仍不存在则抛出异常
                    User user = Optional.ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id)))
                            .orElseThrow(() -> new BusinessException(ResponseStatus.FAIL, "用户不存在"));

                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(user), USER_INFO_TTL, TimeUnit.HOURS);
                    return user;
                });


        return one;
    }


    /**
     * 实现更新用户密码
     *
     * @param userId      用户id
     * @param newPassword 新密码(明文)
     */
    @Override
    public void updatePassword(Long userId, String newPassword) {
        // 根据id查询用户信息
        User user = getUserInfoById(userId);

        // 检查密码强度
        boolean matches = newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+-=;:'\",.<>/?\\[\\]{}|`~]{8,20}$");
        if (!matches) {
            throw new BusinessException(ResponseStatus.FAIL, "密码太弱");
        }

        // 加密，盐值为userId
        newPassword = SmUtil.sm3(userId + newPassword);

        if (StrUtil.equals(user.getPassword(), newPassword)) {
            // 如果新旧密码一致，则返回错误结果
            throw new BusinessException(ResponseStatus.FAIL, "新旧密码不能一致");
        }


        int i = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getPassword, newPassword)
                .set(User::getUpdateTime, LocalDateTime.now())
                .eq(User::getId, userId));

        if (i == 0) {
            log.error("更新用户{}失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "内部错误-更新失败");
        }
    }


    /**
     * 实现更新用户邮箱
     *
     * @param userId   userId
     * @param newEmail 邮箱
     */
    @Override
    public void updateEmail(Long userId, String newEmail) {
        // 查找是否有存在相同的邮箱
        Optional.ofNullable(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, newEmail).in(User::getIsDeleted, 0, 1)))
                .ifPresent(one -> {
                    throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "用户存在");
                });


        int i = userMapper.update(null,
                new LambdaUpdateWrapper<User>()
                        .eq(User::getId, userId)
                        .set(User::getEmail, newEmail)
                        .set(User::getUpdateTime, LocalDateTime.now()));

        if (i == 0) {
            log.error("更新用户{}失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "内部错误-更新失败");
        }


    }


    /**
     * 实现更新用户信息
     *
     * @param user User
     */
    @Override
    public void updateUserInfo(User user) {

        user.setUpdateTime(LocalDateTime.now());
        int i = userMapper.updateById(user);
        if (i == 0) {
            log.error("更新用户({})信息失败失败", user.getId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新失败");
        }
        log.info("更新用户信息{}成功,更新时间：{}", user.getId(), user.getUpdateTime());


    }

    /**
     * 实现更新用户头像
     *
     * @param userId 用户id
     * @param icon   头像
     * @return 头像url
     */
    @Override
    public String updateUserIcon(Long userId, MultipartFile icon) {
        // 根据Id获取当前用户
        User user = getUserInfoById(userId);

        // 异步删除旧的icon,
        String oldIconPath = user.getIcon();
        if (StrUtil.isNotEmpty(oldIconPath) && !StrUtil.equals(oldIconPath, "/icon/default.png")) {
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, FILE_DELETE_QUEUE, user.getIcon());
        }

        // 上传头像
        ResponseResult<String> response = fileService.uploadFile(icon);
        if (!StrUtil.equals(response.getStatus(), "200")) {
            log.error("上传头像失败，响应为====》{}", response);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "上传头像失败");
        }
        String iconPath = response.getData();

        // 更新数据库
        int i = userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getId, userId).set(User::getIcon, iconPath));
        if (i == 0) {
            log.error("更新用户({})头像失败失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新失败");
        }

        // 返回子路径
        return iconPath;
    }

    /**
     * 实现更新用户关注或者粉丝
     *
     * @param name   字段名
     * @param userId id
     * @param type   类型
     */
    @Override
    public void updateFollowOrFans(String name, Long userId, String type) {
        String sql;
        if (StrUtil.equals(type, "insert")) {
            sql = name + "=" + name + "+1";
        } else if (StrUtil.equals(type, "delete")) {
            sql = name + "=" + name + "-1";
        } else {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "参数失败");
        }

        int i = userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getId, userId).setSql(sql));


        if (i == 0) {
            log.error("更新用户具体信息{}失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新失败");
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


    }


    /**
     * 实现根据user创建新用户
     *
     * @param user user
     * @return user
     */
    @Override
    public User createUser(User user) {
        user.setNickName(DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(10).toUpperCase());
        user.setRole(ROLE_USER);

        user.setGender(0);
        user.setFans(0);
        user.setFollows(0);


        int i = userMapper.insert(user);
        if (i == 0) {
            log.error("创建用户{}失败", user.getId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "新建用户失败");
        }

        log.info("创建用户{}成功,创建时间：{}", user.getId(), user.getCreateTime());


        return user;
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

        if (StrUtil.equals(code, cacheCode)) {
            // 删除验证码
            stringRedisTemplate.delete(key + email);
            return true;
        }

        return false;
    }


}
