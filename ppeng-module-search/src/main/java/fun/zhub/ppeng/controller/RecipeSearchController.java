package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.service.RecipeSearchService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜谱搜索接口
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-19
 **/
@RestController
public class RecipeSearchController {

    @Resource
    private RecipeSearchService recipeSearchService;

    /**
     * 根据输入的关键词搜索菜谱
     *
     * @param keyword 关键词
     * @param page    页数
     * @param size    一页所呈现的信息数
     * @return list
     */
    @GetMapping("/search")
    public ResponseResult<List<RecipeDTO>> searchRecipe(@RequestParam("keyword") String keyword, @RequestParam(value = "page",defaultValue = "1") Integer page, @RequestParam(value = "size",defaultValue = "5") Integer size) {

        var list = recipeSearchService.selectRecipeByTitleAndContent(keyword, page, size);

        return ResponseResult.success(list);
    }

}
