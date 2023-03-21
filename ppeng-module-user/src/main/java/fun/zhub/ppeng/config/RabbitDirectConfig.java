package fun.zhub.ppeng.config;


import static com.zhub.ppeng.constant.RabbitConstants.*;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RabbitMQ配置类-DirectExchange模式
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-21
 **/
@Configuration
public class RabbitDirectConfig {



    /**
     * 配置交换机-DirectExchange
     *
     * @return userDirectExchange
     */
    @Bean("userDirectExchange")
    public Exchange userDirectExchange() {

        return ExchangeBuilder.directExchange(USER_EXCHANGE_NAME).durable(true).build();
    }


    /**
     * 配置队列
     *
     * @return userQueue
     */
    @Bean("userCacheQueue")
    public Queue userQueue() {

        return QueueBuilder.durable(USER_CACHE_QUEUE_NAME).build();
    }


    /**
     * 队列和交换机绑定关系
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return getBinding
     */
    @Bean
    public Binding bindingQueue(@Qualifier("userCacheQueue") Queue queue, @Qualifier("userDirectExchange") Exchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_USER_CACHE).noargs();
    }

}
