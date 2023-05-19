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
     * 菜谱添加队列
     *
     * @return RecipeCanalQueue
     */
    @Bean("recipeAddQueue")
    public Queue recipeAddQueue() {

        return QueueBuilder.durable(RECIPE_ADD_QUEUE).build();
    }

    /**
     * 菜谱删除队列
     */
    @Bean("recipeDeleteQueue")
    public Queue recipeDeleteQueue() {

        return QueueBuilder.durable(RECIPE_DELETE_QUEUE).build();
    }

    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding recipeAddQueueBinding(@Qualifier("recipeAddQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_RECIPE_ADD);
    }


    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding recipeDeleteQueueBinding(@Qualifier("recipeAddQueue") Queue queue, @Qualifier("ppengTopicExchange") TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_RECIPE_DELETE);
    }


}
