package fun.zhub.ppeng.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RedisConstants.*;
import static fun.zhub.ppeng.constants.MailConstants.*;

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
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MailService mailService;

    @PostMapping("/register/{mail}")
    public ResponseResult<String> sendRegisterMail(@PathVariable("mail") String mail) {

        // 先查询redis中是否存在该信息
        String key = REGISTER_CODE_KEY + mail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomNumbers(6);

        mailService.sendSimpleMail(MAIL_FROM, MAIL_FROM_NICK, mail, "", REGISTER_SUBJECT, REGISTER_CONTENT_PREFIX + code + CONTENT_SUFFIX);

        stringRedisTemplate.opsForValue().set(key, code, REGISTER_CODE_TTL, TimeUnit.MINUTES);

        return ResponseResult.success(mail);
    }

    @PostMapping("/update/{mail}")
    public ResponseResult<String> sendUpdateMail(@PathVariable("mail") String mail) {

        // 先查询redis中是否存在该信息
        String key = UPDATE_CODE_KEY + mail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomNumbers(6);

        mailService.sendSimpleMail(MAIL_FROM, MAIL_FROM_NICK, mail, "", REGISTER_SUBJECT, UPDATE_CONTENT_PREFIX + code + CONTENT_SUFFIX);

        stringRedisTemplate.opsForValue().set(key, code, UPDATE_CODE_TTL, TimeUnit.MINUTES);

        return ResponseResult.success(mail);
    }

}
