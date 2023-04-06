package fun.zhub.ppeng.rabbitListener;

import static com.zhub.ppeng.constant.RabbitConstants.*;
import fun.zhub.ppeng.service.FollowService;
import fun.zhub.ppeng.service.UserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 监听RabbitMQ队列，加载用户信息到缓存中
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-21
 **/
@Component
@Slf4j
public class LoadingUserInfo {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private FollowService followService;

    /**
     * 监听用户缓存队列(user.cacheQueue)
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = USER_CACHE_QUEUE_NAME),
            exchange = @Exchange(name = USER_EXCHANGE_NAME),
            key = ROUTING_USER_CACHE
    ))
    public void ListenUserCacheQueue(Long id) {
        log.info("开始缓存用户{}的数据", id);
        /*
         * 加载用户其他信息：用户具体信息，具体关注，具体粉丝，具体发布的笔记等
         */
        // 缓存用户基本信息
        userInfoService.getUserInfoById(id);

        // 缓存具体关注
        followService.queryFollowById(id);

        // 缓存具体粉丝数
        followService.queryFansById(id);

        /*
         * TODO 缓存点赞的菜谱
         */

    }
}
