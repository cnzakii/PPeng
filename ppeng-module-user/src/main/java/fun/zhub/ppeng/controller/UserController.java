package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.zhub.ppeng.common.ResponseResult;
import static com.zhub.ppeng.constant.RedisConstants.ROLE_KEY;
import static com.zhub.ppeng.constant.RedisConstants.ROLE_TTL;
import static com.zhub.ppeng.constant.SaTokenConstants.SESSION_USER;
import fun.zhub.ppeng.dto.PasswordLoginFormDTO;
import fun.zhub.ppeng.dto.VerifyCodeLoginFormDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表，包含了用户的基本信息：用户id，手机号，昵称，头像地址等 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private RSA rsa;

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * GET方法获取公钥，用于密码登录
     *
     * @return RSA公钥
     */
    @GetMapping("/login")
    public ResponseResult<String> getPublicKey() {
        return ResponseResult.success(rsa.getPublicKeyBase64());
    }


    /**
     * 通过手机号和密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return authentication
     */
    @PostMapping("/login/by/password")
    public ResponseResult<String> loginByPassword(@Valid PasswordLoginFormDTO loginFormDTO) {

        User user = userService.loginByPassword(loginFormDTO);

        StpUtil.login(user.getId());
        // 将用户基本信保存进session中
        StpUtil.getSession().set(SESSION_USER, user);

        // 设置权限信息
        stringRedisTemplate.opsForValue().set(ROLE_KEY+user.getId(),user.getRole(),ROLE_TTL, TimeUnit.MINUTES);


        /*
         * 异步加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */


        return ResponseResult.success(StpUtil.getTokenValue());
    }


    /**
     * 通过手机号和手机验证码登录或注册
     *
     * @param loginFormDTO 用户验证码登录结构体
     * @return authentication
     */
    @PostMapping("/login/by/code")
    public ResponseResult<String> loginByVerifyCode(@Valid VerifyCodeLoginFormDTO loginFormDTO) {

        User user = userService.loginByVerifyCode(loginFormDTO);

        StpUtil.login(user.getId());
        // 将用户基本信保存进session中
        StpUtil.getSession().set(SESSION_USER, user);

        // 设置权限信息
        stringRedisTemplate.opsForValue().set(ROLE_KEY+user.getId(),user.getRole(),ROLE_TTL, TimeUnit.MINUTES);

        /*
         * 异步加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */

        return ResponseResult.success(StpUtil.getTokenValue());
    }



    /**
     * 用户登出
     *
     * @param token authentication
     * @return success
     */
    @PostMapping("/logout")
    public ResponseResult<String> logout(@RequestHeader("authentication") String token) {
        StpUtil.logoutByTokenValue(token);
        return ResponseResult.success();
    }


}
