package fun.zhub.ppeng.rabbitListener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.annotation.CanalTable;
import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.*;
import static fun.zhub.ppeng.constant.RedisConstants.USER_INFO_TTL;

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


        // 查看用户信息缓存是否存在
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(s)) {
            return;
        }

        log.info("更新用户({})信息缓存", id);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(newData), USER_INFO_TTL, TimeUnit.HOURS);

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
