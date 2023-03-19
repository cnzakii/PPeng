package fun.zhub.ppeng.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import static com.zhub.ppeng.constant.RedisConstants.*;
import static com.zhub.ppeng.constant.RoleConstants.DEFAULT_NICK_NAME_PREFIX;
import static com.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static com.zhub.ppeng.constant.SaTokenConstants.SESSION_USER;
import com.zhub.ppeng.exception.BusinessException;

import fun.zhub.ppeng.dto.PasswordLoginFormDTO;
import fun.zhub.ppeng.dto.VerifyCodeLoginFormDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.mapper.UserMapper;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
    private UserMapper userMapper;

    @Resource
    private Snowflake snowflake;

    @Resource
    private RSA rsa;


    /**
     * 实现通过验证码登录
     *
     * @param loginFormDTO 用户验证码登录结构体
     * @return UserDTO
     */
    @Override
    public User loginByVerifyCode(VerifyCodeLoginFormDTO loginFormDTO) {
        String phone = loginFormDTO.getPhone();
        String code = loginFormDTO.getVerifyCode();
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);

//        // 验证验证码
//        if (!StrUtil.equals(code, cacheCode)) {
//            // 不一致，抛出异常
//            throw new BusinessException(ResponseStatus.FAIL, "验证码不正确");
//        }

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
            log.info(password);
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
            stringRedisTemplate.opsForValue().set(USER_ROLE+user.getId(),user.getRole(),USER_ROLE_TTL, TimeUnit.DAYS);
        }



        /*
         * 异步加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */


        return StpUtil.getTokenValue();
    }
}
