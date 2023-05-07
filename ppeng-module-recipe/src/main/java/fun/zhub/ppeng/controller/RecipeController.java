package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.PushRecipeDTO;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


/**
 * 菜谱接口
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


    /**
     * 根据用户id获取菜谱列表
     *
     * @param userId 用户id
     * @param page   当前页数
     * @param size   一页的菜谱数量
     * @return list
     */
    @GetMapping("/list/{userId}")
    public ResponseResult<List<RecipeDTO>> queryRecipeListByUserId(@PathVariable("userId") String userId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {


        var list = recipeService.getRecipeListByUserId(userId, page, size)
                .stream()
                .map(recipe -> BeanUtil.copyProperties(recipe, RecipeDTO.class))
                .toList();

        return ResponseResult.success(list);
    }

    /**
     * 获取推荐列表--普通菜谱
     *
     * @param page 当前页数
     * @param size 一页的菜谱数量
     * @return list
     */
    @GetMapping("/recommend/common")
    public ResponseResult<List<RecipeDTO>> queryRecommendCommonRecipeList(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {


        return ResponseResult.success();
    }

    /**
     * 获取推荐列表--专业菜谱
     *
     * @param page 当前页数
     * @param size 一页的菜谱数量
     * @return list
     */
    @GetMapping("/recommend/professional")
    public ResponseResult<List<RecipeDTO>> queryRecommendProfessionalRecipeList(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue ="5") Integer size) {
        var list = recipeService.getRecommendnRecipeList(1, page, size);

        return ResponseResult.success();
    }


    /**
     * 删除菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @DeleteMapping("/delete/{recipeId}")
    public ResponseResult<String> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());

        recipeService.deleteRecipeById(id, recipeId);

        return ResponseResult.success();
    }


}
