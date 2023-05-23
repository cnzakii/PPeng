package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeTypeDTO;
import fun.zhub.ppeng.service.RecipeTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 根据id获取菜谱类型名字
     *
     * @param id 菜谱类型id
     * @return 菜谱类型名字
     */
    @GetMapping("/name")
    public ResponseResult<String> getNameById(@RequestParam("id") Integer id) {
        String name = recipeTypeService.getNameById(id);
        return ResponseResult.success(name);
    }


    /**
     * 获取所有菜谱类型 父类的集合
     *
     * @return list
     */
    @GetMapping("/list/parent")
    public ResponseResult<List<RecipeTypeDTO>> getParentTypeList() {
        var list = recipeTypeService.getParentRecipeTypeList();
        return ResponseResult.success(list);
    }
}
