package fun.zhub.ppeng.service.impl;


import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.Like;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.LikeMapper;
import fun.zhub.ppeng.service.LikeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fun.zhub.ppeng.constant.RedisConstants.USER_LIKE_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.USER_LIKE_TTL;

/**
 * <p>
 * 点赞菜谱表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LikeMapper likeMapper;

    /**
     * 实现点赞菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    @Override
    public void addLikedRecipe(Long userId, Long recipeId) {
        String key = USER_LIKE_KEY + userId;
        // 查询是否已经点赞
        Boolean liked = isLiked(userId, recipeId);
        if (BooleanUtil.isTrue(liked)) {
            throw new BusinessException(ResponseStatus.FAIL, "已经点赞了");
        }

        //  存入数据库并写入redis
        Like like = new Like();
        like.setUserId(userId);
        like.setRecipeId(recipeId);
        int i = likeMapper.insert(like);
        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "添加失败");
        }

        // 查看是否有-1元素，有则清除
        Boolean b = stringRedisTemplate.opsForSet().isMember(key, "-1");
        if (BooleanUtil.isTrue(b)) {
            stringRedisTemplate.opsForSet().remove(key, "-1");
        }

        stringRedisTemplate.opsForSet().add(key, String.valueOf(recipeId));
        stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);
    }

    /**
     * 实现查询该用户所点赞的菜谱列表
     *
     * @param userId userId
     * @return Set
     */
    @Override
    public Set<String> queryLikedRecipeSet(Long userId) {
        String key = USER_LIKE_KEY + userId;
        // 先从Redis里面查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        if (members != null && !members.isEmpty()) {
            // 查看是否包含-1，也就是说该用户当前没有关注
            boolean b = members.contains(String.valueOf(-1));
            stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);
            if (b && members.size() == 1) {
                return null;
            } else {
                return members;
            }
        }

        // 如果Redis里面没有，则从数据库中获取
        List<Like> likeList = likeMapper.selectList(new QueryWrapper<Like>().eq("user_id", userId));

        if (likeList == null || likeList.isEmpty()) {
            // 如果没有，这插入一条为-1的数据，然后存入Redis
            stringRedisTemplate.opsForSet().add(key, "-1");
            stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);
            return null;
        }

        // 如果有，则存入Redis并返回
        String[] likeArray = likeList.stream()
                .map(like -> String.valueOf(like.getRecipeId()))
                .toArray(String[]::new);


        Set<String> set = Stream.iterate(0, i -> i < likeArray.length, i -> i + 1)
                .map(i -> likeArray[i])
                .collect(Collectors.toSet());


        stringRedisTemplate.opsForSet().add(key, likeArray);
        stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);

        return set;
    }

    /**
     * 实现删除已点赞的菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    @Override
    public void deleteLikedRecipe(Long userId, Long recipeId) {
        // 查询是否已经点赞
        Boolean liked = isLiked(userId, recipeId);
        if (BooleanUtil.isFalse(liked)) {
            throw new BusinessException(ResponseStatus.FAIL, "尚未点赞");
        }

        int i = likeMapper.delete(new QueryWrapper<Like>().eq("user_id", userId).eq("recipe_id", recipeId));

        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "删除失败");
        }
        stringRedisTemplate.opsForSet().remove(USER_LIKE_KEY + userId, String.valueOf(recipeId));
        stringRedisTemplate.expire(USER_LIKE_KEY + userId, USER_LIKE_TTL, TimeUnit.MINUTES);

    }


    /**
     * 查询是否点赞过
     *
     * @param userId   用户id
     * @param recipeId 菜谱
     * @return true or false
     */
    @Override
    public Boolean isLiked(Long userId, Long recipeId) {
        String key = USER_LIKE_KEY + userId;
        // 先从Redis里面查询
        Set<String> members = stringRedisTemplate.opsForSet().members(key);

        if (members != null && !members.isEmpty()) {
            // 刷新过期时间
            stringRedisTemplate.expire(key, USER_LIKE_TTL, TimeUnit.MINUTES);
            return members.contains(String.valueOf(recipeId));

        }

        // 没有则查询数据库
        Set<String> set = queryLikedRecipeSet(userId);

        if(set==null||set.isEmpty()){
            return false;
        }

        return set.contains(String.valueOf(recipeId));
    }


}
