package fun.zhub.ppeng.service.impl;


import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.mapper.FollowMapper;
import fun.zhub.ppeng.service.FollowService;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zhub.ppeng.constant.RedisConstants.*;

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

    @Resource
    private UserService userService;



    /**
     * 实现添加关注
     *
     * @param userId   用户id
     * @param followId 关注id
     */
    @Override
    public void addFollow(Long userId, Long followId) {
        String key = USER_FOLLOWS_KEY + userId;
        Boolean isFollow = isFollow(userId, followId);

        if (BooleanUtil.isTrue(isFollow)) {
            throw new BusinessException(ResponseStatus.FAIL, "已经关注过该用户");
        }

        // 写入数据库，存入redis
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowId(followId);
        follow.setCreateTime(LocalDateTime.now());
        int i = followMapper.insert(follow);
        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "添加失败");
        }

        // 累加粉丝数和关注数
        userService.updateFollowOrFans("follows", userId, "insert");
        userService.updateFollowOrFans("fans", followId, "insert");


        // 查看是否有-1元素，有则清除
        Boolean b = stringRedisTemplate.opsForSet().isMember(key, "-1");
        if (BooleanUtil.isTrue(b)) {
            stringRedisTemplate.opsForSet().remove(key, "-1");
        }

        stringRedisTemplate.opsForSet().add(key, String.valueOf(followId));
        stringRedisTemplate.expire(key, USER_FOLLOWS_TTL, TimeUnit.MINUTES);

    }


    /**
     * 实现根据id查询用户的所有关注
     *
     * @param id 用户id
     * @return 关注列表
     */
    @Override
    public Set<String> queryFollowById(Long id) {
        return queryById(USER_FOLLOWS_KEY, USER_FOLLOWS_TTL, TimeUnit.MINUTES, "user_id", id);
    }


    /**
     * 实现根据id查询用户的所有粉丝
     *
     * @param id 用户id
     * @return 粉丝列表
     */
    @Override
    @Cacheable(cacheNames = "userFans", key = "#id")
    public Set<String> queryFansById(Long id) {
        return queryById(USER_FANS_KEY, USER_FANS_TTL, TimeUnit.MINUTES, "follow_id", id);
    }

    /**
     * 实现删除关注功能
     *
     * @param userId   用户id
     * @param followId 关注id
     */
    @Override
    public void deleteFollow(Long userId, Long followId) {
        String key = USER_FOLLOWS_KEY + userId;
        Boolean isFollow = isFollow(userId, followId);
        if (BooleanUtil.isFalse(isFollow)) {
            throw new BusinessException(ResponseStatus.FAIL, "尚未关注该用户");
        }

        int i = followMapper.delete(new QueryWrapper<Follow>().eq("user_id", userId).eq("follow_id", followId));

        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "删除失败");
        }

        // 减少粉丝数和关注数
        userService.updateFollowOrFans("follows", userId, "delete");
        userService.updateFollowOrFans("fans", followId, "delete");


        // 删除redis缓存并刷新过期时间
        stringRedisTemplate.opsForSet().remove(key, String.valueOf(followId));
        stringRedisTemplate.expire(key, USER_FOLLOWS_TTL, TimeUnit.MINUTES);

    }


    /**
     * 实现根据id查询用户粉丝或者关注
     *
     * @param prefixKey redis Key前缀
     * @param ttl       redis Key的过期时间
     * @param timeUnit  时间单位
     * @param name      数据库中的id字段名
     * @param id        id
     * @return set
     */
    @Override
    public Set<String> queryById(String prefixKey, Long ttl, TimeUnit timeUnit, String name, Long id) {


        String key = prefixKey + id;
        // 先从redis中查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);


        // 如果不为空,刷新缓存
        if (members != null && !members.isEmpty()) {
            // 查看是否包含-1，也就是说该用户当前没有关注
            boolean b = members.contains(String.valueOf(-1));
            if (b && members.size() == 1) {
                return null;
            } else {
                return members;
            }
        }

        // 没有再从数据库查询
        List<Follow> list = followMapper.selectList(new QueryWrapper<Follow>().eq(name, id));

        if (list == null || list.isEmpty()) {
            // 如果没有，这插入一条为-1的数据，然后存入Redis
            stringRedisTemplate.opsForSet().add(key, "-1");
            stringRedisTemplate.expire(key, ttl, timeUnit);
            return null;
        }

        Set<String> set = list.stream()
                .map(obj -> Objects.equals(name, "follow_id") ? String.valueOf(obj.getFollowId()) : String.valueOf(obj.getUserId()))
                .collect(Collectors.toSet());

        String[] followArray = list.stream()
                .map(obj -> Objects.equals(name, "follow_id") ? String.valueOf(obj.getFollowId()) : String.valueOf(obj.getUserId()))
                .toArray(String[]::new);


        stringRedisTemplate.opsForSet().add(key, followArray);
        stringRedisTemplate.expire(key, ttl, timeUnit);

        return set;
    }

    /**
     * 实现判断是否关注
     *
     * @param userId   用户id
     * @param followId 关注id
     * @return true or false
     */
    @Override
    public Boolean isFollow(Long userId, Long followId) {
        String key = USER_FOLLOWS_KEY + userId;

        // 先从Redis里面查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        if (members != null && !members.isEmpty()) {
            // 刷新过期时间
            stringRedisTemplate.expire(key, USER_FOLLOWS_TTL, TimeUnit.MINUTES);
            return members.contains(String.valueOf(followId));
        }

        // 没有则查询数据库
        Set<String> set = queryFollowById(userId);

        return set.contains(String.valueOf(followId));
    }
}
