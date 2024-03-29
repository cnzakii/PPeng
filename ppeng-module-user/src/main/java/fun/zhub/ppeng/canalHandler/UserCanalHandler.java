package fun.zhub.ppeng.canalHandler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.*;

/**
 * user表中数据发生变化 的处理类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-07
 **/
@Component
@CanalTable("t_user")
@Slf4j
public class UserCanalHandler extends AbstractCanalHandler<User> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheManager cacheManager;

    /**
     * 处理新增的数据
     *
     * @param data 插入的数据
     */
    @Override
    public void insert(User data) {

    }

    /**
     * 处理更新的数据
     *
     * @param oldData 旧数据
     * @param newData 新数据
     */
    @Override
    public void update(User oldData, User newData) {
        Long id = newData.getId();
        String key = USER_INFO + id;

        // 如果已经删除，则直接删除缓存
        if (newData.getIsDeleted() == 1) {
            stringRedisTemplate.delete(key);
            return;
        }

        // 查看用户角色是否发生变化,且缓存是否存在
        if (!StrUtil.equals(oldData.getRole(), newData.getRole()) && !StrUtil.isBlank(stringRedisTemplate.opsForValue().get(USER_ROLE + id))) {
            log.info("更新用户{}角色信息信息", id);
            String[] ss = newData.getRole().split(",");
            stringRedisTemplate.opsForSet().add(USER_ROLE + id, ss);
            stringRedisTemplate.expire(USER_ROLE + id, ROLE_USER_TTL, TimeUnit.HOURS);
        }


        // 查看redis用户信息缓存是否存在
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(s)) {
            return;
        }

        log.info("更新用户({})信息Redis缓存", id);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(newData), USER_INFO_TTL, TimeUnit.HOURS);

        // 更新本地缓存
        Cache cache = cacheManager.getCache("userInfo");
        Long userId = oldData.getId();
        if (cache == null) {
            return;
        }

        boolean b = cache.evictIfPresent(userId);
        if (b) {
            log.info("更新用户({})信息本地缓存", id);
        }
        cache.put(userId, newData);
    }


    /**
     * 处理删除的数据
     *
     * @param data 删除操作
     */
    @Override
    public void delete(User data) {

    }
}
