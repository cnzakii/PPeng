package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.RecipeTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜谱类型表 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/recipe/type")
public class RecipeTypeController {

    @Resource
    private RecipeTypeService recipeTypeService;


    @GetMapping("/list")
    public ResponseResult<Map<String, List<String>>> getTotalRecipeTypeList() {
        Map<String, List<String>> map = recipeTypeService.queryTotalNameList();
        return ResponseResult.success(map);
    }
}
