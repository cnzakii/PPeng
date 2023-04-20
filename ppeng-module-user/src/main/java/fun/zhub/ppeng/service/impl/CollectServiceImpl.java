package fun.zhub.ppeng.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.entity.Collect;
import fun.zhub.ppeng.mapper.CollectMapper;
import fun.zhub.ppeng.service.CollectService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zhub.ppeng.constant.RedisConstants.USER_COLLECT_KEY;
import static com.zhub.ppeng.constant.RedisConstants.USER_COLLECT_TTL;

/**
 * <p>
 * 收藏菜谱表 服务实现类
 * </p>
 *
 * @author lbl
 * @since 2023-04-17
 */

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CollectMapper collectMapper;

    /**
     * 实现收藏菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    @Override
    public void addCollectedRecipe(Long userId, Long recipeId) {
        String key = USER_COLLECT_KEY + userId;

        // 查询是否已经收藏
        Boolean collected = isCollected(userId, recipeId);
        if (BooleanUtil.isTrue(collected)) {
            throw new BusinessException(ResponseStatus.FAIL, "已经收藏了");
        }

        //  存入数据库并写入redis
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setRecipeId(recipeId);
        collect.setCreateTime(LocalDateTime.now());
        int i = collectMapper.insert(collect);
        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "收藏失败");
        }

        // 查看是否有-1元素，有则清除
        Boolean b = stringRedisTemplate.opsForSet().isMember(key, "-1");
        if (BooleanUtil.isTrue(b)) {
            stringRedisTemplate.opsForSet().remove(key, "-1");
        }

        stringRedisTemplate.opsForSet().add(key, String.valueOf(recipeId));
        stringRedisTemplate.expire(key, USER_COLLECT_TTL, TimeUnit.MINUTES);
    }

    /**
     * 实现查询该用户所收藏的菜谱列表
     *
     * @param userId 用户id
     * @return Set
     */
    @Override
    public Set<String> queryCollectedRecipeSet(Long userId) {
        String key = USER_COLLECT_KEY + userId;
        // 先从Redis里面查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        if (members != null && !members.isEmpty()) {
            // 查看是否包含-1，也就是说该用户当前没有关注
            boolean b = members.contains(String.valueOf(-1));
            stringRedisTemplate.expire(key, USER_COLLECT_TTL, TimeUnit.MINUTES);
            stringRedisTemplate.expire(key, USER_COLLECT_TTL, TimeUnit.MINUTES);
            if (b && members.size() == 1) {
                return null;
            } else {
                return members;
            }
        }
        List<Collect> collectList = collectMapper.selectList(new QueryWrapper<Collect>().eq("user_id", userId));
        if (collectList == null || collectList.isEmpty()) {
            // 如果没有，这插入一条为-1的数据，然后存入Redis
            stringRedisTemplate.opsForSet().add(key, "-1");
            stringRedisTemplate.opsForSet().add(key);
            stringRedisTemplate.expire(key, USER_COLLECT_TTL, TimeUnit.MINUTES);
            return null;
        }

        // 如果有，则存入Redis并返回
        String[] collectArray = collectList.stream()
                .map(collect -> String.valueOf(collect.getRecipeId()))
                .toArray(String[]::new);

        Set<String> set = Stream.iterate(0, i -> i < collectArray.length, i -> i + 1)
                .map(i -> collectArray[i])
                .collect(Collectors.toSet());

        stringRedisTemplate.opsForSet().add(key, collectArray);
        stringRedisTemplate.expire(key, USER_COLLECT_TTL, TimeUnit.MINUTES);

        return set;
    }

    /**
     * 实现删除已收藏的菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    @Override
    public void deleteCollectedRecipe(Long userId, Long recipeId) {
        // 查询是否已经点赞
        Boolean collected = isCollected(userId, recipeId);
        if (BooleanUtil.isFalse(collected)) {
            throw new BusinessException(ResponseStatus.FAIL, "尚未收藏");
        }

        int i = collectMapper.delete(new QueryWrapper<Collect>().eq("user_id", userId).eq("recipe_id", recipeId));

        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "删除失败");
        }
        stringRedisTemplate.opsForSet().remove(USER_COLLECT_KEY + userId, String.valueOf(recipeId));
        stringRedisTemplate.expire(USER_COLLECT_KEY + userId, USER_COLLECT_TTL, TimeUnit.MINUTES);

    }

    /**
     * 查询是否收藏过
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     * @return true or false
     */
    @Override
    public Boolean isCollected(Long userId, Long recipeId) {
        String key = USER_COLLECT_KEY + userId;
        // 先从Redis里面查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        if (members != null && !members.isEmpty()) {
            // 刷新过期时间
            stringRedisTemplate.expire(key, USER_COLLECT_TTL, TimeUnit.MINUTES);
            return members.contains(String.valueOf(recipeId));

        }

        // 没有则查询数据库
        Set<String> set = queryCollectedRecipeSet(userId);

        if (set == null || set.isEmpty()) {
            return false;
        }

        return set.contains(String.valueOf(recipeId));
    }

}
