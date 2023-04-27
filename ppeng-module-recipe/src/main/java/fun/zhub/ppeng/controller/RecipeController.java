package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.PushRecipeDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Resource
    private RecipeService recipeService;


    /**
     * 菜谱上传
     *
     * @param pushRecipeDTO pushRecipeDTO
     * @return recipeId
     */
    @PostMapping("/push")
    public ResponseResult<String> pushRecipe(@Valid @RequestBody PushRecipeDTO pushRecipeDTO) {
        // 验证id是否和token对应的id一致
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        Long userId = pushRecipeDTO.getUserId();
        if (!Objects.equals(id, userId)) {
            return ResponseResult.fail("id错误");
        }
        Recipe recipe = BeanUtil.copyProperties(pushRecipeDTO, Recipe.class);
        recipe.setIsProfessional(0);
        String urls = String.join(",", pushRecipeDTO.getMediaUrl());
        recipe.setMediaUrl(urls);

        Long recipeId = recipeService.createRecipe(recipe);
        return ResponseResult.success(String.valueOf(recipeId));
    }


}
