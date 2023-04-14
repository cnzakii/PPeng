package fun.zhub.ppeng.rabbitListener;

import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.dto.VerifyMailDTO;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.zhub.ppeng.constant.RabbitConstants.*;

/**
 * <p>
 * 监听mail.send.queue队列，异步发送邮件
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@Component
@Slf4j
public class MailListener {

    @Resource
    private MailService mailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MAIL_SEND_QUEUE_NAME),
            exchange = @Exchange(name = PPENG_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ROUTING_MAIL_SEND
    ))
    public void ListenSendMailQueue(String json) {
        VerifyMailDTO mailDTO = JSONUtil.toBean(json, VerifyMailDTO.class);

        log.info(mailDTO.toString());
        mailService.sendEmailVerificationCode(
                mailDTO.getMailFrom(),
                mailDTO.getMailFromNick(),
                mailDTO.getMailTo(),
                mailDTO.getSubject(),
                mailDTO.getType(),
                mailDTO.getVerifyCode()
        );
    }

}
