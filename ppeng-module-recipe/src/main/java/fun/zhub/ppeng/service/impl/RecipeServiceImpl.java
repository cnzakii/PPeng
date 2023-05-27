package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.PageBean;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.constant.SystemConstants;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.dto.RecipeTypeDTO;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_RECOMMEND_COMMON_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_RECOMMEND_PROFESSIONAL_KEY;
import static fun.zhub.ppeng.constant.SystemConstants.PPENG_RESOURCE_URL;

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

        // 如果是图文，保证菜谱标题，内容，配料表，图片路径都存在
        if (recipe.getIsVideo() == 0 && StrUtil.hasEmpty(recipe.getTitle(), recipe.getMaterial(), recipe.getContent(), recipe.getMediaUrl())) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "部分参数为空");
        }

        // 如果是视频，保证菜谱标题，视频路径都存在
        if (recipe.getIsVideo() == 1 && StrUtil.hasEmpty(recipe.getTitle(), recipe.getMediaUrl())) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "部分参数为空");
        }

        recipe.setLikes(0);
        recipe.setCollections(0);
        recipe.setCensorState(0);
        recipe.setIsBaned(0);


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
     * 根据菜谱Id来更新菜谱的点赞数和收藏数
     *
     * @param recipeId 菜谱Id
     * @param field    字段名
     * @param change   变化的数值
     * @return true or false
     */
    @Override
    public Boolean updateRecipeStatsById(Long recipeId, String field, int change) {
        int i = recipeMapper.updateRecipeStatsById(recipeId, field, change);

        return i==1;
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
        Recipe recipe = recipeMapper.selectOne(new LambdaQueryWrapper<Recipe>().eq(Recipe::getId, recipeId));

        if (recipe == null) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱不存在");
        }

        if (!Objects.equals(recipe.getUserId(), userId)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱不属于该用户");
        }
        return recipe;
    }

    /**
     * 实现 根据用户id查找菜谱
     *
     * @param userId    用户id
     * @param timestamp 时间戳
     * @param pageSize  一页所呈现的菜谱数量
     * @return list
     */
    @Override
    public PageBean<RecipeDTO> getRecipeListByUserId(Long userId, Long timestamp, Integer pageSize) {
        Timestamp dateTime = new Timestamp(timestamp);


        List<Recipe> recipeList = recipeMapper.getRecipeListByIdAndTimeLimit("user_id", userId, dateTime, pageSize);


        // 如果为空，则直接返回
        if (CollUtil.isEmpty(recipeList)) {
            return new PageBean<>();
        }

        List<RecipeDTO> list = recipeList.stream()
                .map(this::toRecipeDTO)
                .toList();

        // 获取最小时间戳
        Recipe recipe = recipeList.get(recipeList.size() - 1);
        long milli = recipe.getCreateTime().toInstant(SystemConstants.CST).toEpochMilli();

        return new PageBean<>(list, milli);
    }

    /**
     * 实现 根据菜谱类型id查找菜谱
     *
     * @param typeId    菜谱类型id
     * @param timestamp 时间戳
     * @param pageSize  一页所呈现的菜谱数量
     * @return list
     */
    @Override
    public PageBean<RecipeDTO> getRecipeListByTypeId(Integer typeId, Long timestamp, Integer pageSize) {

        Timestamp dateTime = new Timestamp(timestamp);

        // 根据typeId获取子类菜谱类型集合
        List<RecipeTypeDTO> subTypeList = recipeTypeService.getSubRecipeTypeListByParentId(typeId);

        List<Recipe> recipeList;
        if (CollUtil.isEmpty(subTypeList)) {
            // 如果获取的子类菜谱类型集合为null，说明此菜谱类型Id就是子类菜谱类型Id
            recipeList = recipeMapper.getRecipeListByIdAndTimeLimit("type_id", typeId, dateTime, pageSize);
        } else {
            // 获取所有子类Id集合
            Object[] ids = subTypeList.stream().map(RecipeTypeDTO::getId).toArray();

            recipeList = recipeMapper.selectList(new LambdaQueryWrapper<Recipe>()
                    .in(Recipe::getTypeId, ids)
                    .eq(Recipe::getIsBaned, 0)
                    .lt(Recipe::getCreateTime, dateTime)
                    .orderByDesc(Recipe::getCreateTime)
                    .last(" limit " + pageSize)
            );

        }


        // 如果为空，则直接返回
        if (CollUtil.isEmpty(recipeList)) {
            return new PageBean<>();
        }

        List<RecipeDTO> list = recipeList.stream()
                .map(this::toRecipeDTO)
                .toList();

        // 获取最小时间戳
        Recipe recipe = recipeList.get(recipeList.size() - 1);
        long milli = recipe.getCreateTime().toInstant(SystemConstants.CST).toEpochMilli();

        return new PageBean<>(list, milli);
    }

    /**
     * 实现获取 推荐列表
     *
     * @param isProfessional 是否为专业
     * @param timestamp      最后一篇菜谱的时间戳
     * @param pageSize       一页所呈现的菜谱数量
     * @return Pagebean
     */
    @Override
    public PageBean<RecipeDTO> getRecommendRecipeList(Integer isProfessional, Long timestamp, Integer pageSize) {
        String key = (isProfessional == 1) ? RECIPE_RECOMMEND_PROFESSIONAL_KEY : RECIPE_RECOMMEND_COMMON_KEY;

        Set<TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, timestamp, 0, pageSize);

        // 为null则直接返回
        if (CollUtil.isEmpty(typedTuples)) {
            return new PageBean<>();
        }

        // 初始化成当前时间戳
        long lastTimestamp = System.currentTimeMillis();

        List<Recipe> recipeList = new ArrayList<>(typedTuples.size());
        for (TypedTuple<String> typedTuple : typedTuples) {
            // 获取分数
            lastTimestamp = Objects.requireNonNull(typedTuple.getScore()).longValue();
            recipeList.add(JSONUtil.toBean(typedTuple.getValue(), Recipe.class));
        }

        List<RecipeDTO> list = recipeList.stream().map(this::toRecipeDTO).toList();

        return new PageBean<>(list, lastTimestamp);
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
     * 填充菜谱信息,并转换成RecipeDTO对象
     *
     * @param recipe 菜谱对象
     * @return RecipeDTO
     */
    @Override
    public RecipeDTO toRecipeDTO(Recipe recipe) {
        RecipeDTO recipeDTO = BeanUtil.copyProperties(recipe, RecipeDTO.class);

        Long userId = recipe.getUserId();
        ResponseResult<User> result = userService.getUserInfo(userId);

        // 判断是否请求成功
        if (!StrUtil.equals(result.getStatus(), "200")) {
            log.error("填充菜谱的用户信息失败===》{}", result);
            return recipeDTO;
        }
        // 填入用户信息
        User userInfo = result.getData();
        recipeDTO.setNickName(userInfo.getNickName());
        recipeDTO.setIcon(PPENG_RESOURCE_URL + userInfo.getIcon());

        // 将mediaUrl 字符串转成 String数组
        String[] array = Arrays.stream(recipe.getMediaUrl().trim().split(","))
                .map(s -> PPENG_RESOURCE_URL + s)
                .toArray(String[]::new);
        recipeDTO.setMediaUrl(array);

        // 设置recipeDTO的菜谱类型
        recipeDTO.setType(recipeTypeService.getNameById(recipe.getTypeId()));

        return recipeDTO;
    }


}
