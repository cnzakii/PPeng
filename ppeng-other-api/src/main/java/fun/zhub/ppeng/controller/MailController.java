package fun.zhub.ppeng.controller;

import cn.hutool.core.util.StrUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.user.UserVerifyDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.UserService;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 发送邮箱
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-08
 **/

@RestController
@RequestMapping("/mail")
public class MailController {


    @Resource
    private MailService mailService;

    @Resource
    private UserService userService;

    /**
     * 发送注册邮件，无需token
     *
     * @param mail mail
     * @return success
     */
    @PostMapping("/register/{mail}")
    public ResponseResult<String> sendRegisterMail(@PathVariable("mail") String mail) {


        mailService.sendRegisterEmail(mail);

        return ResponseResult.success();
    }


    /**
     * 发送用户更新邮箱操作，需要token
     *
     * @param userVerifyDTO userVerifyDTO
     * @return success
     */
    @PostMapping("/verify")
    public ResponseResult<String> sendUpdateMail(@RequestBody @Validated UserVerifyDTO userVerifyDTO) {
        Long userId = userVerifyDTO.getUserId();

        // 服务调用user服务，查询该用户信息
        ResponseResult<User> response = userService.getUserInfo(userId);

        // 如果调用失败，直接返回
        if (!StrUtil.equals(response.getStatus(), "200")) {
            return ResponseResult.base(response.getStatus(), null, response.getMessage());
        }

        String userEmail = response.getData().getEmail();

        if (!StrUtil.equals(userEmail, userVerifyDTO.getEmail())) {
            return ResponseResult.fail("邮箱不属于该用户");
        }

        Integer type = userVerifyDTO.getType();

        switch (type) {
            case 0 -> mailService.sendUpdatePasswordEmail(userEmail);
            case 1 -> mailService.sendUpdateEmailEmail(userEmail);
            case 2 -> mailService.sendDeleteUserEmail(userEmail);
        }

        return ResponseResult.success();
    }

}
