package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.ContentCensorDTO;
import fun.zhub.ppeng.dto.UserDTO;
import fun.zhub.ppeng.dto.VerifyEmailDTO;
import fun.zhub.ppeng.dto.login.LoginFormDTO;
import fun.zhub.ppeng.dto.register.RegisterDTO;
import fun.zhub.ppeng.dto.update.UpdateUserEmailDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.dto.update.UserInfoUpdateDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.exception.GlobalBlockHandler;
import fun.zhub.ppeng.service.UserService;
import fun.zhub.ppeng.util.MyBeanUtil;
import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static fun.zhub.ppeng.constant.RabbitConstants.*;
import static fun.zhub.ppeng.constant.RedisConstants.*;
import static fun.zhub.ppeng.constant.SaTokenConstants.*;
import static fun.zhub.ppeng.constant.SystemConstants.PPENG_URL;


/**
 * <p>
 * 用户接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */


@RestController
@RequestMapping("/user")
@Slf4j
@Validated
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
    @SentinelResource(value = "getPublicKey", blockHandlerClass = GlobalBlockHandler.class, blockHandler = "handleCommonBlockException")
    public ResponseResult<String> getPublicKey() {

        return ResponseResult.success(rsa.getPublicKeyBase64());
    }


    /**
     * 通过邮箱和密码注册
     *
     * @param registerDTO 用户密码登录结构体
     * @return p-token
     */
    @PostMapping("/register")
    public ResponseResult<String> registerUser(@RequestBody @Validated RegisterDTO registerDTO) {
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();
        String password = registerDTO.getPassword();

        try {
            password = rsa.decryptStr(password, KeyType.PrivateKey);
        } catch (Exception e) {
            return ResponseResult.fail("密码未加密");
        }

        // 验证邮箱
        Boolean b = userService.verifyEmail(email, code, REGISTER_CODE_KEY);

        if (BooleanUtil.isFalse(b)) {
            return ResponseResult.fail("验证码错误");
        }
        userService.register(email, password);

        return ResponseResult.success();
    }

    /**
     * 通过邮箱和密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return p-token
     */
    @PostMapping("/login/by/password")
    public ResponseResult<String> loginByPassword(@RequestBody @Validated LoginFormDTO loginFormDTO) {
        String email = loginFormDTO.getEmail();
        String password;
        try {
            password = rsa.decryptStr(loginFormDTO.getPassword(), KeyType.PrivateKey);
        } catch (Exception e) {
            return ResponseResult.fail("密码未加密");
        }

        User user = userService.loginByPassword(email, password);

        String token = userService.afterLogin(user);

        return ResponseResult.success(token);
    }

    /**
     * 微信一键登录
     *
     * @param code code
     * @return p-token
     */
    @PostMapping("/login/by/wx/{code}")
    public ResponseResult<String> loginByWeChat(@PathVariable("code") String code) {

        User user = userService.loginByWeChat(code);
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
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_USER_CACHE_DELETE, id);

        return ResponseResult.success();
    }


    /**
     * 获取当前用户信息
     *
     * @return userInfo
     */
    @PostMapping("/current")
    public ResponseResult<UserDTO> getCurrentInfo() {
        Long id = Long.valueOf((String) StpUtil.getLoginId());

        User userInfo = userService.getUserInfoById(id);

        UserDTO userDTO = BeanUtil.copyProperties(userInfo, UserDTO.class);

        // email脱敏
        userDTO.setEmail(DesensitizedUtil.email(userDTO.getEmail()));

        return ResponseResult.success(userDTO);
    }

    /**
     * 根据id获取用户的完全信息 ----》 不对外开放，仅用于服务之间的调用
     *
     * @return userInfo
     */
    @GetMapping("/info/{userId}")
    public ResponseResult<User> getUserInfo(@PathVariable("userId") Long userId) {
        User user = userService.getUserInfoById(userId);
        return ResponseResult.success(user);
    }


    /**
     * 用户身份安全认证
     *
     * @param verifyEmailDTO verifyEmailDTO
     * @return success
     */
    @PostMapping("/safe/verify")
    public ResponseResult<String> verifyIdentity(@RequestBody @Validated VerifyEmailDTO verifyEmailDTO) {
        Long userId = verifyEmailDTO.getUserId();

        // 验证email是否一致
        User user = userService.getUserInfoById(userId);

        if (!StrUtil.equals(verifyEmailDTO.getEmail(), user.getEmail())) {
            return ResponseResult.fail("邮箱错误");
        }

        Integer type = verifyEmailDTO.getType();
        String userEmail = user.getEmail();
        String code = verifyEmailDTO.getCode();

        Boolean b;

        /*
         * 0 更新密码 1 更新邮箱  2 删除账号
         */
        switch (type) {
            case 0 -> {
                b = userService.verifyEmail(userEmail, code, UPDATE_PASSWORD_CODE_KEY);
                if (BooleanUtil.isTrue(b)) {
                    StpUtil.openSafe(SAFE_UPDATE_PASSWORD, SAFE_TIME);
                }
            }
            case 1 -> {
                b = userService.verifyEmail(userEmail, code, UPDATE_EMAIL_CODE_KEY);
                if (BooleanUtil.isTrue(b)) {
                    StpUtil.openSafe(SAFE_UPDATE_EMAIL, SAFE_TIME);
                }
            }
            case 2 -> {
                b = userService.verifyEmail(userEmail, code, DELETE_USER_CODE_KEY);
                if (BooleanUtil.isTrue(b)) {
                    StpUtil.openSafe(SAFE_DELETE_USER, SAFE_TIME);
                }
            }
            default -> throw new BusinessException(ResponseStatus.HTTP_STATUS_400);
        }

        if (BooleanUtil.isFalse(b)) {
            throw new BusinessException(ResponseStatus.FAIL, "验证码错误");
        }


        return ResponseResult.success();
    }


    /**
     * 更新用户密码--需要二次认证
     *
     * @return success
     */
    @PutMapping("/update/password")
    public ResponseResult<String> updateUserPassword(@RequestBody @Validated UpdateUserPasswordDTO userPasswordDTO) {
        Long userId = userPasswordDTO.getUserId();


        try {
            String newPassword = rsa.decryptStr(userPasswordDTO.getNewPassword(), KeyType.PrivateKey);
            userService.updatePassword(userId, newPassword);
        } catch (Exception e) {
            return ResponseResult.fail("密码未加密");
        }

        // 关闭安全认证
        StpUtil.closeSafe(SAFE_UPDATE_PASSWORD);

        return ResponseResult.success();
    }

    /**
     * 更新用户邮箱--需要二次认证
     *
     * @return success
     */
    @PutMapping("/update/email")
    public ResponseResult<String> updateUserEmail(@RequestBody @Validated UpdateUserEmailDTO userEmailDTO) {

        Long userId = userEmailDTO.getUserId();


        String newEmail = userEmailDTO.getEmail();
        String code = userEmailDTO.getCode();

        // 验证邮箱
        Boolean b = userService.verifyEmail(newEmail, code, UPDATE_EMAIL_CODE_KEY);

        if (BooleanUtil.isFalse(b)) {
            return ResponseResult.fail("验证码错误");
        }


        userService.updateEmail(userId, newEmail);

        // 关闭安全认证
        StpUtil.closeSafe(SAFE_UPDATE_EMAIL);
        return ResponseResult.success();
    }


    /**
     * 修改用户信息
     *
     * @param userInfoUpdateDTO userInfoUpdateDTO
     * @return success
     */
    @PutMapping("/current")
    public ResponseResult<String> updateUserInfo(@RequestBody @Validated UserInfoUpdateDTO userInfoUpdateDTO) {

        Long userId = userInfoUpdateDTO.getUserId();

        User user = userService.getUserInfoById(userId);

        MyBeanUtil.copyPropertiesIgnoreNull(userInfoUpdateDTO, user);

        String address = Optional.ofNullable(userInfoUpdateDTO.getAddress())
                .map(list -> String.join(",", list))
                .orElse(null);
        user.setAddress(address);


        userService.updateUserInfo(user);


        String nickName = user.getNickName();
        // 异步审核昵称
        if (StrUtil.isNotEmpty(nickName)) {
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_CONTENT_CENSOR, JSONUtil.toJsonStr(new ContentCensorDTO("nickName", userId, nickName)));
        }

        return ResponseResult.success();
    }


    /**
     * 更新用户头像
     *
     * @param userId 用户id
     * @param icon   头像文件
     * @return success
     */
    @PutMapping("/update/icon/{userId}")
    public ResponseResult<String> updateUserIcon(@PathVariable("userId") @MatchToken Long userId, MultipartFile icon) {


        String iconPath = userService.updateUserIcon(userId, icon);

        // 异步审核图片
        if (StrUtil.isNotEmpty(iconPath)) {
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_CONTENT_CENSOR, JSONUtil.toJsonStr(new ContentCensorDTO("icon", userId, iconPath.replace(PPENG_URL, ""))));
        }

        return ResponseResult.success(PPENG_URL + iconPath);
    }


    /**
     * 删除当前用户--需要二次认证
     *
     * @return success
     */
    @DeleteMapping("/current")
    public ResponseResult<String> deleteUser() {

        Long id = Long.valueOf((String) StpUtil.getLoginId());

        userService.deleteUserById(id);

        StpUtil.logout();

        // 异步删除缓存
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_USER_CACHE_DELETE, id);

        // 关闭安全认证
        StpUtil.closeSafe(SAFE_DELETE_USER);
        return ResponseResult.success();
    }

}
