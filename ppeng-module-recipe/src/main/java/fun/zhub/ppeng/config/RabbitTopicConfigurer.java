package fun.zhub.ppeng.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fun.zhub.ppeng.constant.RabbitConstants.*;

/**
 * <p>
 * RabbitMQ配置类-TopicExchange模式
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-08
 **/
@Configuration
public class RabbitTopicConfigurer {

    /**
     * 配置交换机-TopicExchange
     *
     * @return ppengTopicExchange
     */
    @Bean("ppengTopicExchange")
    public TopicExchange ppengTopicExchange() {

        return ExchangeBuilder.topicExchange(PPENG_EXCHANGE).durable(true).build();
    }


    /**
     * 菜谱模块Canal监听队列
     *
     * @return RecipeCanalQueue
     */
    @Bean("recipeCanalQueue")
    public Queue recipeCanalQueue() {

        return QueueBuilder.durable(RECIPE_CANAL_QUEUE).build();
    }


    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding recipeCanalQueueBinding(@Qualifier("recipeCanalQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_CANAL_DATA);
    }


}
