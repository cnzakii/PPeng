package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.feign.RecipeService;

/**
 * RecipeService 调用失败的兜底方法
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
public class RecipeFallBackServiceImpl implements RecipeService {
    @Override
    public Recipe queryRecipeById(Long recipeId) {
        return null;
    }
}
