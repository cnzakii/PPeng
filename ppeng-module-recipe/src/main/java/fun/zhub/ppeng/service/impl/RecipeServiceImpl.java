package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.dto.RecommendRecipeDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.feign.UserService;
import fun.zhub.ppeng.mapper.RecipeMapper;
import fun.zhub.ppeng.service.RecipeService;
import fun.zhub.ppeng.service.RecipeTypeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_RECOMMEND_COMMON_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_RECOMMEND_PROFESSIONAL_KEY;

/**
 * RecipeService
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
@Slf4j
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

    @Resource
    private RecipeMapper recipeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RecipeTypeService recipeTypeService;

    @Resource
    private UserService userService;

    @Resource
    private Snowflake snowflake;


    /**
     * 实现创建菜谱操作
     *
     * @param recipe 没有recipeId的recipe对象
     * @return 菜谱id
     */
    @Override
    public Long createRecipe(Recipe recipe) {
        // 查询typeid是否正确
        if (StrUtil.isEmpty(recipeTypeService.getNameById(recipe.getTypeId()))) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱类型错误");
        }

        if (StrUtil.hasEmpty(recipe.getTitle(), recipe.getMaterial(), recipe.getContent(), recipe.getMediaUrl())) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "参数为空");
        }

        long id = snowflake.nextId();

        recipe.setId(id);
        recipe.setLikes(0);
        recipe.setCollections(0);
        recipe.setCensorState(0);
        recipe.setIsBaned(0);
        recipe.setIsDeleted(0);
        recipe.setCreateTime(LocalDateTime.now());


        // 插入数据库
        int i = recipeMapper.insert(recipe);
        if (i != 1) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "添加失败");
        }

        return recipe.getId();
    }


    /**
     * 实现更新菜谱审核状态
     *
     * @param recipeId    菜谱id
     * @param censorState 审核状态
     * @param isBaned     是否违规
     */
    @Override
    public void updateCensorState(Long recipeId, Integer censorState, Integer isBaned) {
        int i = recipeMapper.update(null,
                new LambdaUpdateWrapper<Recipe>()
                        .eq(Recipe::getId, recipeId)
                        .set(Recipe::getCensorState, censorState)
                        .set(Recipe::getIsBaned, isBaned)
                        .set(Recipe::getUpdateTime, LocalDateTime.now()));
        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱不存在");
        }

    }

    /**
     * 实现删除菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    @Override
    public void deleteRecipeById(Long userId, Long recipeId) {
        int i = recipeMapper.delete(new LambdaQueryWrapper<Recipe>().eq(Recipe::getUserId, userId).eq(Recipe::getId, recipeId));

        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "删除菜谱失败");
        }
    }

    /**
     * 实现根据用户id和菜谱id
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     * @return recipe
     */
    @Override
    public Recipe getRecipeByUserIdAndRecipeId(Long userId, Long recipeId) {
        Recipe recipe = recipeMapper.selectOne(new LambdaQueryWrapper<Recipe>().eq(Recipe::getId, recipeId).eq(Recipe::getUserId, userId));

        if (recipe == null) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱不存在");
        }

        return recipe;
    }

    /**
     * 实现 根据用户id查找菜谱
     *
     * @param userId   用户id
     * @param pageNum  当前页数
     * @param pageSize 一页所呈现的菜谱数量
     * @return list
     */
    @Override
    public List<Recipe> getRecipeListByUserId(String userId, Integer pageNum, Integer pageSize) {
        Page<Recipe> page = new Page<>(pageNum, pageSize, false);
        Page<Recipe> recipePage = recipeMapper.selectPage(page,
                new LambdaQueryWrapper<Recipe>()
                        .eq(Recipe::getUserId, userId)
                        .eq(Recipe::getIsBaned, "0")
                        .orderByDesc(Recipe::getCreateTime));

        return recipePage.getRecords();
    }

    /**
     * 实现获取 推荐列表
     *
     * @param isProfessional 是否为专业
     * @param timestamp      最后一篇菜谱的时间戳
     * @param pageSize       一页所呈现的菜谱数量
     * @return RecommendRecipeDTO
     */
    @Override
    public RecommendRecipeDTO getRecommendRecipeList(Integer isProfessional, Long timestamp, Integer pageSize) {
        String key = (isProfessional == 1) ? RECIPE_RECOMMEND_PROFESSIONAL_KEY : RECIPE_RECOMMEND_COMMON_KEY;

        Set<TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, timestamp, 0, pageSize);

        // 为null则直接返回
        if (CollUtil.isEmpty(typedTuples)) {
            return null;
        }

        // 初始化成当前时间戳
        long lastTimestamp = System.currentTimeMillis();

        List<Recipe> recipeList = new ArrayList<>(typedTuples.size());
        for (TypedTuple<String> typedTuple : typedTuples) {
            // 获取分数
            lastTimestamp = Objects.requireNonNull(typedTuple.getScore()).longValue();
            recipeList.add(JSONUtil.toBean(typedTuple.getValue(), Recipe.class));
        }

        List<RecipeDTO> list = recipeList.stream().map(this::fillRecipeUserInfo).toList();
        RecommendRecipeDTO recommendRecipeDTO = new RecommendRecipeDTO();
        recommendRecipeDTO.setRecipeDTOList(list);
        recommendRecipeDTO.setMinTimestamp(lastTimestamp);


        return recommendRecipeDTO;
    }

    /**
     * 实现更新菜谱
     *
     * @param recipe recipe
     */
    @Override
    public void updateRecipe(Recipe recipe) {
        recipe.setUpdateTime(LocalDateTime.now());

        int i = recipeMapper.updateById(recipe);

        if (i == 0) {
            log.error("更新菜谱{}失败", recipe.getId());
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新菜谱失败");
        }
        log.info("更新菜谱{}成功,更新时间：{}", recipe.getId(), recipe.getUpdateTime());

    }

    /**
     * 填充菜谱的用户信息
     *
     * @param recipe 菜谱对象
     * @return RecipeDTO
     */
    @Override
    public RecipeDTO fillRecipeUserInfo(Recipe recipe) {
        RecipeDTO recipeDTO = BeanUtil.copyProperties(recipe, RecipeDTO.class);

        Long userId = recipe.getUserId();
        ResponseResult<User> result = userService.getUserInfo(userId);

        // 判断是否请求成功
        if (!StrUtil.equals(result.getStatus(), "200")) {
            log.error("填充菜谱的用户信息失败===》{}", result);
            return recipeDTO;
        }

        User userInfo = result.getData();
        recipeDTO.setNickName(userInfo.getNickName());
        recipeDTO.setIcon(userInfo.getIcon());
        return recipeDTO;
    }


}
