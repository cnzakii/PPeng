package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.dto.ContentCensorDTO;
import fun.zhub.ppeng.dto.UserInfoDTO;
import fun.zhub.ppeng.dto.login.LoginFormDTO;
import fun.zhub.ppeng.dto.register.RegisterDTO;
import fun.zhub.ppeng.dto.update.UpdateUserEmailDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.dto.update.UserInfoUpdateDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.exception.GlobalBlockHandler;
import fun.zhub.ppeng.service.UserInfoService;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zhub.ppeng.constant.RabbitConstants.*;
import static com.zhub.ppeng.constant.SystemConstants.PPENG_URL;


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
@Slf4j
public class UserController {
    @Resource
    private RSA rsa;

    @Resource
    private UserService userService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserInfoService userInfoService;


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
     * 微信一键登录
     *
     * @param code code
     * @return p-token
     */
    @PostMapping("/login/by/wx/{code}")
    public ResponseResult<Map<String, Object>> loginByWeChat(@PathVariable("code") String code) {

        Map<String, Object> map = userService.loginByWeChat(code);
        User user = BeanUtil.toBean(map.get("user"), User.class);

        String token = userService.afterLogin(user);

        /*
         * {"token": "", "isFirst": true}
         */
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("isFirst", map.get("isFirst"));
        return ResponseResult.success(result);
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
     * @return userBaseInfo
     */
    @PostMapping("/current")
    public ResponseResult<UserInfoDTO> getCurrentInfo() {
        Long id = Long.valueOf((String) StpUtil.getLoginId());


        UserInfoDTO userInfoDTO = userService.getUserInfoById(id);

        // email脱敏
        userInfoDTO.setEmail(DesensitizedUtil.email(userInfoDTO.getEmail()));

        return ResponseResult.success(userInfoDTO);
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
     * 修改用户信息
     *
     * @param userInfoUpdateDTO userInfoUpdateDTO
     * @return success
     */
    @PutMapping("/current")
    public ResponseResult<String> updateUserInfo(@RequestBody @Valid UserInfoUpdateDTO userInfoUpdateDTO) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        Long userId = userInfoUpdateDTO.getUserId();
        if (!Objects.equals(id, userId)) {
            return ResponseResult.fail("id错误");
        }

        // 更新user表
        String nickName = userInfoUpdateDTO.getNickName();
        String icon = userInfoUpdateDTO.getIcon();
        userService.updateNickNameAndIcon(userId, nickName, icon);

        // 更新userInfo表
        String[] address = userInfoUpdateDTO.getAddress();
        String introduce = userInfoUpdateDTO.getIntroduce();
        Byte gender = userInfoUpdateDTO.getGender();
        LocalDate birthday = userInfoUpdateDTO.getBirthday();
        userInfoService.updateUserInfo(id, Arrays.toString(address), introduce, gender, birthday);


        // 异步审核昵称
        if (StrUtil.isNotEmpty(nickName)) {
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_CONTENT_CENSOR, JSONUtil.toJsonStr(new ContentCensorDTO("nickName", id, nickName)));
        }

        // 异步审核图片
        if (StrUtil.isNotEmpty(icon)) {
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_CONTENT_CENSOR, JSONUtil.toJsonStr(new ContentCensorDTO("icon", id, icon.replace(PPENG_URL, ""))));
        }


        return ResponseResult.success();
    }


    /**
     * 删除当前用户
     *
     * @return success
     */
    @DeleteMapping("/current")
    public ResponseResult<String> deleteUser() {

        Long id = Long.valueOf((String) StpUtil.getLoginId());

        userService.deleteUserById(id);

        StpUtil.logout();

        return ResponseResult.success();
    }

}
