package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.dto.TextContentCensorDTO;
import fun.zhub.ppeng.dto.UserDTO;
import fun.zhub.ppeng.dto.login.LoginFormDTO;
import fun.zhub.ppeng.dto.register.RegisterDTO;
import fun.zhub.ppeng.dto.update.UpdateUserEmailDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.zhub.ppeng.constant.RabbitConstants.*;


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
    private RabbitTemplate rabbitTemplate;


    /**
     * GET方法获取公钥，用于密码登录
     *
     * @return RSA公钥
     */
    @GetMapping("/rsa")
    public ResponseResult<String> getPublicKey() {
        /*
         * TODO 写入JVM缓存
         */
        return ResponseResult.success(rsa.getPublicKeyBase64());
    }

    /**
     * 通过邮箱和密码注册
     *
     * @param registerDTO 用户密码登录结构体
     * @return p-token
     */
    @PostMapping("/register")
    public ResponseResult<String> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return ResponseResult.success();
    }


    /**
     * 通过邮箱和密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return p-token
     */
    @PostMapping("/login/by/password")
    public ResponseResult<String> loginByPassword(@Valid @RequestBody LoginFormDTO loginFormDTO) {
        User user = userService.loginByPassword(loginFormDTO);

        String token = userService.afterLogin(user);

        return ResponseResult.success(token);
    }


    /**
     * 用户登出
     *
     * @return success
     */
    @PostMapping("/logout")
    public ResponseResult<String> logout() {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        StpUtil.logout(id);

        /*
         * 异步删除用户其他缓存信息，如角色信息，具体粉丝等
         */
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE_NAME, ROUTING_USER_CACHE_DEL, id);

        return ResponseResult.success();
    }


    /**
     * 获取当前用户基本信息
     *
     * @return userBaseInfo
     */
    @PostMapping("/current")
    public ResponseResult<UserDTO> getCurrentInfo() {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        // 获取经过脱敏处理后的userInfo
        UserDTO userDTO = userService.getUserBaseInfoById(id);

        return ResponseResult.success(userDTO);
    }


    /**
     * 更新用户密码
     *
     * @return success
     */
    @PutMapping("update/password")
    public ResponseResult<String> updateUserPassword(@RequestBody @Valid UpdateUserPasswordDTO userPasswordDTO) {

        userService.updatePassword(userPasswordDTO);


        return ResponseResult.success();
    }

    /**
     * 更新用户邮箱
     *
     * @return success
     */
    @PutMapping("update/email")
    public ResponseResult<String> updateUserEmail(@RequestBody @Valid UpdateUserEmailDTO userEmailDTO) {

        userService.updateEmail(userEmailDTO);

        return ResponseResult.success();
    }


    /**
     * 更新用户昵称
     *
     * @param nickName 昵称
     * @return success
     */
    @PutMapping("update/nick/name")
    public ResponseResult<String> updateUserNickName(@RequestParam(value = "nickName") String nickName) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());

        userService.updateNickNameById(id, nickName);

        /*
         * 使用MQ将昵称传给第三方审核接口进行审核。
         */
        TextContentCensorDTO censorDTO = new TextContentCensorDTO("nickName", id, nickName);
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE_NAME, ROUTING_TEXT_CENSOR, JSONUtil.toJsonStr(censorDTO));


        return ResponseResult.success();
    }


    /**
     * 更新用户头像
     *
     * @param icon 头像
     * @return path
     */
    @PutMapping("update/icon")
    public ResponseResult<String> updateUserIcon(@RequestParam(value = "icon") @NotNull(message = "新头像不能为空") MultipartFile icon) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());

        String path = userService.updateIconById(id, icon);

        return ResponseResult.success(path);
    }


    /**
     * 删除当前用户
     *
     * @return success
     */
    @DeleteMapping("/current")
    public ResponseResult<String> DeleteUser() {
        /*
         * TODO 可能需要进行二次认证
         */

        Long id = Long.valueOf((String) StpUtil.getLoginId());

        userService.deleteUserById(id);

        StpUtil.logout();

        return ResponseResult.success();
    }

}
