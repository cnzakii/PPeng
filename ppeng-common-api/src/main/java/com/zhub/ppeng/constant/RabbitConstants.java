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
    String PPENG_EXCHANGE_NAME = "ppeng.topic.exchange";

    /**
     * user信息缓存队列
     */
    String USER_CACHE_QUEUE_NAME = "user.cache.queue";


    /**
     * user信息缓存删除队列
     */
    String USER_CACHE_DELETE_QUEUE_NAME = "user.cache.delete.queue";

    /*
     * user信息更新缓存
     */
    String USER_CACHE_UPDATE_QUEUE_NAME = "user.cache.update.queue";


    /**
     * routingKey 添加用户缓存
     */
    String ROUTING_USER_CACHE = "user.cache";

    /**
     * routingKey: 删除用户缓存
     */
    String ROUTING_USER_CACHE_DEL = "user.cache.delete";

    /**
     * routingKey: 更新用户缓存
     */
    String ROUTING_USER_CACHE_UPDATE = "user.cache.update";


}
