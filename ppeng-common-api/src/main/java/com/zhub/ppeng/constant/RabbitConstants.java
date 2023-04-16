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
    String PPENG_EXCHANGE = "ppeng.topic.exchange";


    /**
     * user信息缓存队列
     */
    String USER_CACHE_QUEUE = "user.cache.queue";


    /**
     * user信息缓存删除队列
     */
    String USER_CACHE_DELETE_QUEUE = "user.cache.delete.queue";

    /*
     * user信息更新缓存队列
     */
    String USER_CACHE_UPDATE_QUEUE = "user.cache.update.queue";

    /*
     * 发送邮件队列
     */
    String MAIL_SEND_QUEUE = "mail.send.queue";

    /**
     * 内容审核队列
     */
    String CONTENT_CENSOR_QUEUE = "content.censor.queue";


    /**
     * 文件删除删除队列
     */
    String FILE_DELETE_QUEUE = "file.delete.queue";


    /**
     * routingKey 添加用户缓存
     */
    String ROUTING_USER_CACHE = "user.cache";

    /**
     * routingKey: 删除用户缓存
     */
    String ROUTING_USER_CACHE_DELETE = "user.cache.delete";

    /**
     * routingKey: 更新用户缓存
     */
    String ROUTING_USER_CACHE_UPDATE = "user.cache.update";

    /**
     * routingKey: 发送邮件
     */
    String ROUTING_MAIL_SEND = "mail.send";

    /**
     * routingKey: 内容审核
     */
    String ROUTING_CONTENT_CENSOR = "content.censor";

    /**
     * routingKey: 删除文件
     */
    String ROUTING_FILE_DELETE = "file.delete";


}
