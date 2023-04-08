package fun.zhub.ppeng.service.impl;


import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
    private JavaMailSender mailSender;


    /**
     * 实现发送简单文字邮件
     *
     * @param mailFrom     发件人邮箱
     * @param mailFromNick 发件人昵称
     * @param mailTo       收件人邮箱
     * @param cc           抄送人邮箱(可为空，方法内部处理)
     * @param subject      主题
     * @param content      内容
     */
    @Override
    public void sendSimpleMail(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        //邮件发件人
        message.setFrom(mailFrom);
        //邮件收件人 1或多个
        message.setTo(mailTo);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);

        mailSender.send(message);

        log.info("发送邮件成功:{}->{}", mailFrom, mailTo);


    }
}
