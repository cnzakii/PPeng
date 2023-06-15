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
 * @since 2023-03-21
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
     * 配置用户缓存队列
     *
     * @return userQueue
     */
    @Bean("userCacheQueue")
    public Queue userCacheQueue() {


        return QueueBuilder.durable(USER_CACHE_QUEUE).build();
    }


    /**
     * 配置用户缓存删除队列
     *
     * @return userCacheDeleteQueue
     */
    @Bean("userCacheDeleteQueue")
    public Queue userCacheDeleteQueue() {

        return QueueBuilder.durable(USER_CACHE_DELETE_QUEUE).build();
    }

    /**
     * 用户模块Canal监听队列
     *
     * @return UserCanalQueue
     */
    @Bean("userCanalQueue")
    public Queue userCanalQueue() {

        return QueueBuilder.durable(USER_CANAL_QUEUE).build();
    }



    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding userCacheQueueBinding(@Qualifier("userCacheQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_USER_CACHE);
    }

    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding userCacheDeleteQueueBinding(@Qualifier("userCacheDeleteQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_USER_CACHE_DELETE);
    }

    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding userCacheUpdateQueueBinding(@Qualifier("userCanalQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_CANAL_DATA);
    }



}
