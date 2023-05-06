package fun.zhub.ppeng.rabbitListener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static fun.zhub.ppeng.constant.RedisConstants.*;

/**
 * 更新redis缓存
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
     * 更新用户缓存
     *
     * @param oldList 旧数据集合
     * @param newList 新数据集合
     */
    public void updateUserCache(List<User> oldList, List<User> newList) {
        IntStream.range(0, newList.size())
                .forEach(i -> updateUserCache(oldList.get(i), newList.get(i)));
    }


    /**
     * 更新用户缓存
     *
     * @param oldUser 旧数据
     * @param newUser 新数据
     */
    public void updateUserCache(User oldUser, User newUser) {
        Long id = newUser.getId();
        String key = USER_INFO + id;

        // 如果已经删除，则直接删除缓存
        if (newUser.getIsDeleted() == 1) {
            stringRedisTemplate.delete(key);
            return;
        }

        // 查看用户角色是否发生变化,且缓存是否存在
        if (!StrUtil.equals(oldUser.getRole(), newUser.getRole()) && !StrUtil.isBlank(stringRedisTemplate.opsForValue().get(USER_ROLE + id))) {
            log.info("更新用户{}角色信息信息", id);
            String[] ss = newUser.getRole().split(",");
            stringRedisTemplate.opsForSet().add(USER_ROLE + id, ss);
            stringRedisTemplate.expire(USER_ROLE + id, ROLE_USER_TTL, TimeUnit.HOURS);
        }


        // 查看用户信息缓存是否存在
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(s)) {
            return;
        }

        log.info("更新用户({})信息缓存", id);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(newUser), USER_INFO_TTL, TimeUnit.HOURS);


    }


    /**
     * 更新用户粉丝数
     *
     * @param newList 新数据集合
     */
    public void insertFansCache(List<Follow> newList) {
        newList.forEach(this::insertFansCache);
    }


    /**
     * 更新用户粉丝数
     *
     * @param newFollow 新数据
     */
    public void insertFansCache(Follow newFollow) {
        String key = USER_FANS_KEY + newFollow.getFollowId();

        // 先检查是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (CollUtil.isEmpty(members)) {
            return;
        }
        log.info("更新用户({})粉丝数缓存", newFollow.getFollowId());
        stringRedisTemplate.opsForSet().add(key, String.valueOf(newFollow.getUserId()));
        stringRedisTemplate.expire(key, USER_FANS_TTL, TimeUnit.MINUTES);

    }

    /**
     * 删除用户粉丝数
     *
     * @param deletedList 删除的数据集合
     */
    public void deleteFansCache(List<Follow> deletedList) {
        deletedList.forEach(this::deleteFansCache);
    }


    /**
     * 删除用户粉丝数
     *
     * @param deletedFollow 删除的数据
     */
    public void deleteFansCache(Follow deletedFollow) {
        String key = USER_FANS_KEY + deletedFollow.getFollowId();

        // 先检查是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (CollUtil.isEmpty(members)) {
            return;
        }
        log.info("更新用户({})粉丝数缓存", deletedFollow.getFollowId());
        stringRedisTemplate.opsForSet().remove(key, String.valueOf(deletedFollow.getUserId()));
        stringRedisTemplate.expire(key, USER_FANS_TTL, TimeUnit.MINUTES);
    }
}
