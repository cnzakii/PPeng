package fun.zhub.ppeng.rabbitListener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static fun.zhub.ppeng.constant.RabbitConstants.*;

/**
 * <p>
 * 监听RabbitMQ队列，更新缓存
 * <p>
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


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = USER_CACHE_UPDATE_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_CACHE_UPDATE
    ))
    public void listenCanalQueue(String s) {
        JSONObject object = new JSONObject(s);


        String type = object.getStr("type");
        String table = object.getStr("table");
        JSONArray data = object.getJSONArray("data");
        Object one = data.get(0);

        if (StrUtil.equals(type, "UPDATE")) {
            switch (table) {
                case "t_user" -> {
                    User user = BeanUtil.toBean(one, User.class);
                    // 更新Redis
                    redisHandler.updateUser(user);
                }
                case "t_follow" -> {
                    Follow follow = BeanUtil.toBean(one, Follow.class);
                    redisHandler.updateFan(follow);
                }
            }
        }

    }
}
