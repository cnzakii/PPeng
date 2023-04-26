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
     * 实现上传图文菜谱
     *
     * @param userId         用户id
     * @param typeId         类型id
     * @param title          标题
     * @param material       配料
     * @param content        内容
     * @param images         图片
     * @param isProfessional 是否专业
     * @return recipeId
     */
    @Override
    public Long saveImageRecipe(Long userId, Integer typeId, String title, String material, String content, String images, Integer isProfessional) {
        Recipe recipe = new Recipe();
        recipe.setId(userId);

        // 查询typeid是否正确
        if (StrUtil.isEmpty(recipeTypeService.getNameById(typeId))) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱类型错误");
        }
        recipe.setTypeId(typeId);

        if (StrUtil.hasEmpty(title, material, content, images)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "参数为空");
        }

        recipe.setTitle(title);
        recipe.setMaterial(material);
        recipe.setContent(content);
        recipe.setImges(images);
        recipe.setIsProfessional(isProfessional);
        recipe.setIsBaned(0);
        recipe.setCensorState(0);

        recipe.setId(snowflake.nextId());
        recipe.setCreateTime(LocalDateTime.now());

        // 插入数据库
        int i = recipeMapper.insert(recipe);
        if (i != 1) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "添加失败");
        }

        return recipe.getId();
    }

    /**
     * 实现上传视频菜谱
     *
     * @param userId         用户id
     * @param typeId         类型
     * @param title          标题
     * @param material       配料表
     * @param video          视频
     * @param isProfessional 是否专业
     * @return recipeId
     */
    @Override
    public Long saveVideoRecipe(Long userId, Integer typeId, String title, String material, String video, Integer isProfessional) {
        Recipe recipe = new Recipe();
        recipe.setId(userId);

        // 查询typeid是否正确
        if (StrUtil.isEmpty(recipeTypeService.getNameById(typeId))) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱类型错误");
        }
        recipe.setTypeId(typeId);

        if (StrUtil.hasEmpty(title, material, video)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "参数为空");
        }

        recipe.setTitle(title);
        recipe.setMaterial(material);
        recipe.setVideo(video);
        recipe.setIsProfessional(isProfessional);
        recipe.setIsBaned(0);
        recipe.setCensorState(0);

        recipe.setId(snowflake.nextId());
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
