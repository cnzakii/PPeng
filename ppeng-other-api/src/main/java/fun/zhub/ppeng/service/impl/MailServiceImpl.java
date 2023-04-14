package fun.zhub.ppeng.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.dto.VerifyMailDTO;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RabbitConstants.PPENG_EXCHANGE_NAME;
import static com.zhub.ppeng.constant.RabbitConstants.ROUTING_MAIL_SEND;
import static com.zhub.ppeng.constant.RedisConstants.*;
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
    private RabbitTemplate rabbitTemplate;

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
        boolean b = mail.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        if (!b) {
            throw new BusinessException(ResponseStatus.FAIL, "邮件格式错误");
        }
        // 先查询redis中是否存在该信息
        String key = REGISTER_CODE_KEY + mail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomNumbers(6);

        VerifyMailDTO mailDTO = new VerifyMailDTO(MAIL_FROM, MAIL_FROM_NICK, mail, REGISTER_SUBJECT, REGISTER_TYPE, code);

        /*
         * MQ异步处理
         */
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE_NAME, ROUTING_MAIL_SEND, JSONUtil.toJsonStr(mailDTO));


        stringRedisTemplate.opsForValue().set(key, code, REGISTER_CODE_TTL, TimeUnit.MINUTES);

    }

    /**
     * 实现发送修改的验证码邮件
     *
     * @param mail mail
     */
    @Override
    public void sendUpdateEmail(String mail) {
        boolean b = mail.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        if (!b) {
            throw new BusinessException(ResponseStatus.FAIL, "邮件格式错误");
        }
        // 先查询redis中是否存在该信息
        String key = UPDATE_CODE_KEY + mail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomNumbers(6);

        VerifyMailDTO mailDTO = new VerifyMailDTO(MAIL_FROM, MAIL_FROM_NICK, mail, UPDATE_SUBJECT, UPDATE_TYPE, code);

        /*
         * MQ异步处理
         */
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE_NAME, ROUTING_MAIL_SEND, JSONUtil.toJsonStr(mailDTO));


        stringRedisTemplate.opsForValue().set(key, code, UPDATE_CODE_TTL, TimeUnit.MINUTES);

    }

    /**
     * 实现发送模版邮件
     *
     * @param mailFrom     发件人邮箱
     * @param mailFromNick 发件人昵称
     * @param mailTo       收件人邮箱
     * @param subject      主题
     * @param type         操作类型：注册账号 or 修改账号
     * @param verifyCode   验证码
     */
    @Override
    public void sendEmailVerificationCode(String mailFrom, String mailFromNick, String mailTo, String subject, String type, String verifyCode) {

        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(verifyCode.split("")));
        context.setVariable("type", type);

        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("EmailVerificationCode", context);
        MimeMessage message = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 昵称
            helper.setFrom(mailFromNick + " <" + mailFrom + ">");
            // 收件人
            helper.setTo(mailTo);
            // 主题
            helper.setSubject(subject);

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

}
