package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.feign.RecipeService;

/**
 * RecipeService 调用失败的兜底方法
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-26
 **/
public class RecipeFallbackServiceImpl implements RecipeService {
    @Override
    public boolean updateRecipeStatsById(Long recipeId, String field, int change) {
        return false;
    }
}
