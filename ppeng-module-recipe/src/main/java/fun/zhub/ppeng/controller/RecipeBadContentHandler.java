package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜谱违规信息处理
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/
@RestController
@RequestMapping("/handler/recipe")
public class RecipeBadContentHandler {

    @Resource
    private RecipeService recipeService;


    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱
     *
     * @param type     类型：admin 人工审核 ai 机器审核
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/ban/{type}/{recipeId}")
    public ResponseResult<String> setAccessible(@PathVariable("type") String type, @PathVariable("recipeId") Long recipeId) {

        /*
         * TODO
         */

        return ResponseResult.success();
    }

    /**
     * 放行菜谱，人工审核或者机器审核通过时调用
     *
     * @param type     类型：admin 人工审核 ai 机器审核
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/allow/{type}/{recipeId}")
    public ResponseResult<String> setInaccessible(@PathVariable("type") String type, @PathVariable("recipeId") Long recipeId) {
        /*
         * TODO
         */
        return ResponseResult.success();
    }


}
