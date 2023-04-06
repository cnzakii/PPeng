package fun.zhub.ppeng.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import static com.zhub.ppeng.constant.RedisConstants.*;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.mapper.FollowMapper;
import fun.zhub.ppeng.service.FollowService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 实现FollowService interface
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FollowMapper followMapper;

    /**
     * 实现根据id查询用户的所有关注
     *
     * @param id 用户id
     * @return 关注列表
     */
    @Override
    public Set<String> queryFollowById(Long id) {

        return queryById(USER_FOLLOWS, USER_FOLLOWS_TTL, TimeUnit.MINUTES, "user_id", id);
    }


    /**
     * 实现根据id查询用户的所有粉丝
     *
     * @param id 用户id
     * @return 粉丝列表
     */
    @Override
    public Set<String> queryFansById(Long id) {


        return queryById(USER_FANS, USER_FANS_TTL, TimeUnit.MINUTES, "follow_id", id);
    }

    /**
     * 实现根据id查询用户粉丝或者关注
     *
     * @param prefixKey redis Key前缀
     * @param TTL       redis Key的过期时间
     * @param timeUnit  时间单位
     * @param name      数据库中的id字段名
     * @param id        id
     * @return set
     */
    @Override
    public Set<String> queryById(String prefixKey, Long TTL, TimeUnit timeUnit, String name, Long id) {
        Set<String> set = new HashSet<>();

        String key = prefixKey + id;
        // 先从redis中查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);


        // 如果不为空,刷新缓存
        if (!CollUtil.isEmpty(members)) {
            stringRedisTemplate.expire(key, TTL, timeUnit);
            return set;
        }

        // 没有再从数据库查询
        List<Follow> list = followMapper.selectList(new QueryWrapper<Follow>().eq(name, id));



        for (Follow follow : list) {
            String followId = String.valueOf(follow.getFollowId());
            set.add(followId);
            stringRedisTemplate.opsForSet().add(key, followId);
        }

        stringRedisTemplate.expire(key, TTL, timeUnit);

        return set;
    }
}
