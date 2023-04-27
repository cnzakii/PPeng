package fun.zhub.ppeng.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.RecipeMapper;
import fun.zhub.ppeng.service.RecipeService;
import fun.zhub.ppeng.service.RecipeTypeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

    @Resource
    private RecipeMapper recipeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RecipeTypeService recipeTypeService;

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
}
