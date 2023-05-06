package fun.zhub.ppeng.rabbitListener;

import cn.hutool.core.util.StrUtil;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.util.MyCanalUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static fun.zhub.ppeng.constant.RabbitConstants.*;

/**
 * 监听RabbitMQ队列，更新缓存
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-13
 **/
@Component
@Slf4j
public class UserCacheUpdateListener {

    @Resource
    private RedisHandler redisHandler;


    /**
     * Canal消息监听队列
     *
     * @param json Canal传输的json字符串
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = USER_CACHE_UPDATE_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_CACHE_UPDATE
    ))
    public void listenCanalQueue(String json) {

        // 获取操作类型-INSERT UPDATE DELETE
        String type = MyCanalUtil.getType(json);

        // 获取表名
        String table = MyCanalUtil.getTable(json);

        // user表更新数据时
        if (StrUtil.equals(type, "UPDATE") && StrUtil.equals(table, "t_user")) {
            // 获取旧数据
            List<User> oldData = MyCanalUtil.getOldData(json, User.class);
            // 获取新数据
            List<User> newData = MyCanalUtil.getChangeData(json, User.class);

            // 更新Redis
            redisHandler.updateUserCache(oldData, newData);
            return;
        }

        // follow表 插入 数据时
        if (StrUtil.equals(type, "INSERT") && StrUtil.equals(table, "t_follow")) {
            // 获取新数据
            List<Follow> newData = MyCanalUtil.getChangeData(json, Follow.class);
            // 更新Redis
            redisHandler.insertFansCache(newData);
            return;
        }

        // follow表 删除数据时
        if (StrUtil.equals(type, "DELETE") && StrUtil.equals(table, "t_follow")) {
            // 获取删除数据
            List<Follow> deletedData = MyCanalUtil.getChangeData(json, Follow.class);
            // 更新Redis
            redisHandler.deleteFansCache(deletedData);
        }

    }
}

