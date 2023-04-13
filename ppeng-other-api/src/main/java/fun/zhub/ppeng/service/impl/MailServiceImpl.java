package fun.zhub.ppeng.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RedisConstants.*;
import static com.zhub.ppeng.constant.RedisConstants.UPDATE_CODE_TTL;
import static fun.zhub.ppeng.constants.MailConstants.*;

/**
 * <p>
 * 邮件发送实现类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-08
 **/
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private TemplateEngine templateEngine;


    /**
     * 实现发送注册邮件
     *
     * @param mail mail
     */
    @Override
    public void sendRegisterEmail(String mail) {
        // 先查询redis中是否存在该信息
        String key = REGISTER_CODE_KEY + mail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomNumbers(6);

        /*
         * TODO MQ异步处理
         */

        sendEmailVerificationCode(MAIL_FROM, mail, code, "修改账号");

        stringRedisTemplate.opsForValue().set(key, code, REGISTER_CODE_TTL, TimeUnit.MINUTES);

    }

    /**
     * 实现发送修改的验证码邮件
     *
     * @param mail mail
     */
    @Override
    public void sendUpdateEmail(String mail) {
        // 先查询redis中是否存在该信息
        String key = UPDATE_CODE_KEY + mail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomNumbers(6);

        /*
         * TODO MQ异步处理
         */

        sendEmailVerificationCode(MAIL_FROM, mail, code, "修改账号");

        stringRedisTemplate.opsForValue().set(key, code, UPDATE_CODE_TTL, TimeUnit.MINUTES);

    }

    /*
     * TODO 修改传递参数
     */
    @Override
    public void sendEmailVerificationCode(String mailFrom, String toAddress, String verifyCode, String type) {

        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(verifyCode.split("")));
        context.setVariable("type", type);

        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("EmailVerificationCode", context);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(toAddress);
            helper.setSubject("注册验证码");
            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

}
