package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;

import cn.hutool.crypto.asymmetric.RSA;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.UserDTO;
import fun.zhub.ppeng.dto.login.PasswordLoginFormDTO;
import fun.zhub.ppeng.dto.login.VerifyCodeLoginFormDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPhoneDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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


    /**
     * GET方法获取公钥，用于密码登录
     *
     * @return RSA公钥
     */
    @GetMapping("/rsa")
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
    public ResponseResult<String> loginByPassword(@Valid @RequestBody PasswordLoginFormDTO loginFormDTO) {
        User user = userService.loginByPassword(loginFormDTO);

        String token = userService.afterLogin(user);

        return ResponseResult.success(token);
    }


    /**
     * 通过手机号和手机验证码登录或注册
     *
     * @param loginFormDTO 用户验证码登录结构体
     * @return authentication
     */
    @PostMapping("/login/by/code")
    public ResponseResult<String> loginByVerifyCode(@Valid @RequestBody VerifyCodeLoginFormDTO loginFormDTO) {

        User user = userService.loginByVerifyCode(loginFormDTO);

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
        StpUtil.logout();
        /*
         * TODO 异步删除用户其他缓存信息，如角色信息，具体粉丝等
         */


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

        StpUtil.logout();

        return ResponseResult.success();
    }

    /**
     * 更新用户手机号
     *
     * @return success
     */
    @PutMapping("update/phone")
    public ResponseResult<String> updateUserPhone(@RequestBody @Valid UpdateUserPhoneDTO userPhoneDTO) {

        userService.updatePhone(userPhoneDTO);

        StpUtil.logout();

        return ResponseResult.success();
    }


    /**
     * 更新用户昵称
     *
     * @param nickName 昵称
     * @return success
     */
    @PutMapping("update/nick/name")
    public ResponseResult<String> updateUserNickName(@RequestParam(value = "nickName") @NotNull(message = "新昵称不能为空") @Max(20) String nickName) {
        Long id = (Long) StpUtil.getLoginId();

        userService.updateNickNameById(id, nickName);

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
        Long id = (Long) StpUtil.getLoginId();

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

        Long id = (Long) StpUtil.getLoginId();

        userService.deleteUserById(id);

        StpUtil.logout();

        return ResponseResult.success();
    }

}
