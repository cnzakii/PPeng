package fun.zhub.ppeng.canalHandler;

import cn.hutool.core.collection.CollUtil;
import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.entity.Follow;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.USER_FANS_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.USER_FANS_TTL;

/**
 * follow表中数据发生变化 的处理类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-07
 **/
@Component
@CanalTable("t_follow")
@Slf4j
public class FollowCanalHandler extends AbstractCanalHandler<Follow> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 处理新增的数据
     *
     * @param data 插入的数据
     */
    @Override
    public void insert(Follow data) {
        String key = USER_FANS_KEY + data.getFollowId();

        // 先检查是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (CollUtil.isEmpty(members)) {
            return;
        }
        log.info("更新用户({})粉丝数缓存", data.getFollowId());
        stringRedisTemplate.opsForSet().add(key, String.valueOf(data.getUserId()));
        stringRedisTemplate.expire(key, USER_FANS_TTL, TimeUnit.MINUTES);
    }

    /**
     * 处理更新的数据
     *
     * @param oldData 旧数据
     * @param newData 新数据
     */
    @Override
    public void update(Follow oldData, Follow newData) {

    }

    /**
     * 处理删除的数据
     *
     * @param data 删除操作
     */
    @Override
    public void delete(Follow data) {
        String key = USER_FANS_KEY + data.getFollowId();

        // 先检查是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (CollUtil.isEmpty(members)) {
            return;
        }
        log.info("更新用户({})粉丝数缓存", data.getFollowId());
        stringRedisTemplate.opsForSet().remove(key, String.valueOf(data.getUserId()));
        stringRedisTemplate.expire(key, USER_FANS_TTL, TimeUnit.MINUTES);
    }
}
