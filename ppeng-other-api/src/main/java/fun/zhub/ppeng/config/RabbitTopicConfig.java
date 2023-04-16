package fun.zhub.ppeng.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zhub.ppeng.constant.RabbitConstants.*;

/**
 * <p>
 * RabbitMQ配置类-TopicExchange模式
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-21
 **/
@Configuration
public class RabbitTopicConfig {


    /**
     * 配置交换机-ppengTopicExchange
     *
     * @return TopicExchange
     */
    @Bean("ppengTopicExchange")
    public TopicExchange ppengTopicExchange() {

        return ExchangeBuilder.topicExchange(PPENG_EXCHANGE).durable(true).build();
    }


    /**
     * 配置邮件发送队列
     *
     * @return sendMailQueue
     */
    @Bean("sendMailQueue")
    public Queue sendMailQueue() {

        return QueueBuilder.durable(MAIL_SEND_QUEUE).build();
    }


    /**
     * 配置文本内容审核队列
     *
     * @return textContentCensorQueue
     */
    @Bean("textContentCensorQueue")
    public Queue textContentCensorQueue() {

        return QueueBuilder.durable(CONTENT_CENSOR_QUEUE).build();
    }

    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding userCacheQueueBinding(@Qualifier("sendMailQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_MAIL_SEND);
    }

    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding textContentCensorQueueBinding(@Qualifier("textContentCensorQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_CONTENT_CENSOR);
    }


}
