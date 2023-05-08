package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.dto.RecommendRecipeDTO;
import fun.zhub.ppeng.entity.Recipe;

import java.util.List;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface RecipeService extends IService<Recipe> {


    /**
     * 创建菜谱对象
     *
     * @param recipe 没有recipeId的recipe对象
     * @return recipeId
     */
    Long createRecipe(Recipe recipe);


    /**
     * 更新菜谱审核状态
     *
     * @param recipeId    菜谱id
     * @param censorState 审核状态
     * @param isBaned     是否违规
     */
    void updateCensorState(Long recipeId, Integer censorState, Integer isBaned);

    /**
     * 删除菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    void deleteRecipeById(Long userId, Long recipeId);


    /**
     * 根据用户id和菜谱id查询菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     * @return recipe
     */
    Recipe getRecipeByUserIdAndRecipeId(Long userId, Long recipeId);


    /**
     * 根据用户id查询菜谱
     *
     * @param userId   用户id
     * @param pageNum  当前页数
     * @param pageSize 一页所呈现的菜谱数量
     * @return list
     */
    List<Recipe> getRecipeListByUserId(String userId, Integer pageNum, Integer pageSize);

    /**
     * 获取推荐菜谱列表
     *
     * @param isProfessional 是否为专业
     * @param timestamp      最后一篇菜谱的时间戳
     * @param pageSize       一页所呈现的菜谱数量
     * @return recommendRecipeDTO
     */
    RecommendRecipeDTO getRecommendRecipeList(Integer isProfessional, Long timestamp, Integer pageSize);


    /**
     * 更新recipe
     *
     * @param recipe recipe
     */
    void updateRecipe(Recipe recipe);


    /**
     * 填充菜谱的用户信息
     *
     * @param recipe 菜谱对象
     * @return recipeDTO
     */
    RecipeDTO fillRecipeUserInfo(Recipe recipe);


}
