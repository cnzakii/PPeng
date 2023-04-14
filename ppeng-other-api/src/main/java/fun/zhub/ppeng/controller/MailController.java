package fun.zhub.ppeng.controller;

import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @PostMapping("/register/{mail}")
    public ResponseResult<String> sendRegisterMail(@PathVariable("mail") String mail) {


        mailService.sendRegisterEmail(mail);

        return ResponseResult.success();
    }

    @PostMapping("/update/{mail}")
    public ResponseResult<String> sendUpdateMail(@PathVariable("mail") String mail) {


        mailService.sendUpdateEmail(mail);

        return ResponseResult.success();
    }

}
