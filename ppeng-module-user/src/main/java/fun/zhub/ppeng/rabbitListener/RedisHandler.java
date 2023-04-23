package fun.zhub.ppeng.rabbitListener;

import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.*;

/**
 * <p>
 * 更新redis缓存
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-13
 **/

@Component
@Slf4j
public class RedisHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 更新用户基本信息
     *
     * @param newUser newUser
     */
    public void updateUser(User newUser) {
        Long id = newUser.getId();

        log.info("更新用户({})信息缓存", id);

        if (newUser.getIsDeleted() == 1) {
            stringRedisTemplate.delete(USER_INFO + id);
        } else {
            stringRedisTemplate.opsForValue().set(USER_INFO + id, JSONUtil.toJsonStr(newUser), USER_INFO_TTL, TimeUnit.HOURS);
        }

    }


    /**
     * 更新用户关注和粉丝
     *
     * @param follow follow
     */
    public void updateFan(Follow follow) {
        String key = USER_FANS_KEY + follow.getFollowId();
        // 先检查是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (members != null && !members.isEmpty()) {
            stringRedisTemplate.opsForSet().add(key, String.valueOf(follow.getUserId()));
            stringRedisTemplate.expire(key, USER_FANS_TTL, TimeUnit.MINUTES);
        }


    }


}
