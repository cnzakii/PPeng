package com.zhub.ppeng.constant;

/**
 * <p>
 * RabbitMQ常量
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-21
 **/
public interface RabbitConstants {
    /**
     * user模块交换机名称
      */
    String USER_EXCHANGE_NAME = "user.direct.exchange";

    /**
     * user信息缓存队列
     */
    String USER_CACHE_QUEUE_NAME = "user.cache.queue";

    /**
     * user模块的routingKey
     */
    String ROUTING_USER_CACHE = "user.cache";

    String ROUTING_USER_CACHE_DEL = "user.cache.delete";



}
