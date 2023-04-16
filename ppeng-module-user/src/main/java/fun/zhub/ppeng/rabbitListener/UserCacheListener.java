package fun.zhub.ppeng.rabbitListener;


import fun.zhub.ppeng.service.FollowService;
import fun.zhub.ppeng.service.LikeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.zhub.ppeng.constant.RabbitConstants.*;
import static com.zhub.ppeng.constant.RedisConstants.*;

/**
 * <p>
 * 监听RabbitMQ队列，加载用户信息到缓存中, 从缓存中删除用户信息
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-21
 **/
@Component
@Slf4j
public class UserCacheListener {


    @Resource
    private FollowService followService;

    @Resource
    private LikeService likeService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 监听用户缓存队列(user.cache.queue)，加载用户信息到缓存中
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = USER_CACHE_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_USER_CACHE
    ))
    public void listenUserCacheQueue(Long id) {
        log.info("开始缓存用户{}的数据", id);
        /*
         * 加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */

        // 缓存具体关注
        followService.queryFollowById(id);

        // 缓存具体粉丝数
        followService.queryFansById(id);


        // 缓存点赞的菜谱
        likeService.queryLikedRecipeSet(id);

    }

    /**
     * 监听用户缓存队列(user.cache.delete.queue)，从缓存中删除用户信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = USER_CACHE_DELETE_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_USER_CACHE_DELETE
    ))
    public void listenUserCacheDeleteQueue(Long id) {
        log.info("开始删除用户{}的缓存数据", id);
        /*
         * 删除用户缓存信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */


        // 删除用户信息
        stringRedisTemplate.delete(USER_INFO + id);

        // 删除具体关注
        stringRedisTemplate.delete(USER_FOLLOWS_KEY + id);

        // 删除具体粉丝
        stringRedisTemplate.delete(USER_FANS_KEY + id);

        // 删除缓存点赞的菜谱
        stringRedisTemplate.delete(USER_LIKE_KEY + id);

    }


}
