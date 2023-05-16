package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeCensor;
import fun.zhub.ppeng.feign.RecipeCensorService;
import org.springframework.stereotype.Component;

/**
 * RecipeCensorService 调用失败的兜底方法
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/
@Component
public class RecipeCensorFallBackServiceImpl implements RecipeCensorService {

    @Override
    public Recipe queryRecipeById(Long recipeId) {
        return null;
    }

    @Override
    public RecipeCensor getRecipeCensorById(Long recipeId) {
        return null;
    }

    @Override
    public ResponseResult<String> setInaccessible(RecipeCensorResultDTO resultDTO) {
        return ResponseResult.fail("recipe模块调用失败");

    }

    @Override
    public ResponseResult<String> setaccessible(RecipeCensorResultDTO resultDTO) {
        return ResponseResult.fail("recipe模块调用失败");
    }
}
