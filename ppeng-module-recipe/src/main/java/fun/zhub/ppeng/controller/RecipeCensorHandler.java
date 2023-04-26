package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.service.RecipeCensorService;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
public class RecipeCensorHandler {

    @Resource
    private RecipeService recipeService;

    @Resource
    private RecipeCensorService censorService;


    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/ban")
    public ResponseResult<String> setInAccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO) {
        Integer censorState = resultDTO.getCensorState();
        Long recipeId = resultDTO.getRecipeId();


        recipeService.updateCensorState(recipeId, censorState, 1);
        // 如果人工复审（CensorState=3）仍不通过审核，则进一步执行删除操作
        if (censorState == 3) {
            boolean b = recipeService.removeById(recipeId);
            if (!b) {
                return ResponseResult.fail("删除菜谱失败");
            }
        }

        // 将审核结果保存
        String censorResult = resultDTO.getCensorResult();
        Long censorId = resultDTO.getCensorId();
        LocalDateTime censorTime = resultDTO.getCensorTime();
        censorService.saveCensorResult(recipeId, censorResult, censorId, censorTime, censorState);

        return ResponseResult.success();
    }

    /**
     * 放行菜谱，人工审核或者机器审核通过时调用
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/allow")
    public ResponseResult<String> setaccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO) {
        Integer censorState = resultDTO.getCensorState();
        Long recipeId = resultDTO.getRecipeId();

        recipeService.updateCensorState(recipeId, censorState, 0);


        // 将审核结果保存
        String censorResult = resultDTO.getCensorResult();
        Long censorId = resultDTO.getCensorId();
        LocalDateTime censorTime = resultDTO.getCensorTime();
        censorService.saveCensorResult(recipeId, censorResult, censorId, censorTime, censorState);

        return ResponseResult.success();
    }


}
