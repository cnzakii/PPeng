package fun.zhub.ppeng.canalHandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.entity.Like;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.USER_LIKE_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.USER_LIKE_TTL;

/**
 * like表中数据发生变化 的处理类
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-26
 **/
@Component
@CanalTable("t_like")
@Slf4j
public class LikeCanalHandler extends AbstractCanalHandler<Like> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 处理新增的数据
     *
     * @param data 插入的数据
     */
    @Override
    public void insert(Like data) {
        String key = USER_LIKE_KEY + data.getUserId();
        // 查看是否有-1元素，有则清除
        Boolean b = stringRedisTemplate.opsForSet().isMember(key, "-1");
        if (BooleanUtil.isTrue(b)) {
            stringRedisTemplate.opsForSet().remove(key, "-1");
        }

        log.info("刷新用户({})点赞菜谱缓存", data.getUserId());
        stringRedisTemplate.opsForSet().add(key, String.valueOf(data.getRecipeId()));
        stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);
    }

    /**
     * 处理更新的数据
     *
     * @param oldData 旧数据
     * @param newData 新数据
     */
    @Override
    public void update(Like oldData, Like newData) {

    }

    /**
     * 处理删除的数据
     *
     * @param data 删除操作
     */
    @Override
    public void delete(Like data) {
        String key = USER_LIKE_KEY + data.getUserId();

        // 检查用户的like缓存是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        // 不存在则不做任何处理
        if (CollUtil.isEmpty(members)) {
            return;
        }

        // 删除对应recipeId并刷新缓存
        log.info("刷新用户({})点赞菜谱缓存", data.getUserId());
        stringRedisTemplate.opsForSet().remove(key, String.valueOf(data.getRecipeId()));
        stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);
    }
}
