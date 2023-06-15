package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeCensor;
import fun.zhub.ppeng.service.RecipeCensorService;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static fun.zhub.ppeng.constant.RedisConstants.*;

/**
 * 菜谱审查接口
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@RestController
@RequestMapping("/recipe/censor")
@Slf4j
public class RecipeCensorController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RecipeService recipeService;

    @Resource
    private RecipeCensorService censorService;

    /**
     * 根据菜谱id获取审查结果---仅限服务间调用
     */
    @GetMapping("/info/{recipeId}")
    public RecipeCensor getRecipeCensorById(@PathVariable("recipeId") Long recipeId) {
        return censorService.getById(recipeId);
    }


    /**
     * 举报菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/report/{recipeId}")
    public ResponseResult<String> reporteRecipe(@PathVariable("recipeId") Long recipeId) {
        /*
         * 添加到Redis的菜谱举报Zset集合中，value为菜谱id，score为当前时间戳
         */
        stringRedisTemplate.opsForZSet().addIfAbsent(REPORT_RECIPE_KEY, String.valueOf(recipeId), System.currentTimeMillis());

        return ResponseResult.success();
    }

    /**
     * 用户申述菜谱
     *
     * @param sign 标识
     * @return success
     */
    @PostMapping("/appeal")
    public ResponseResult<String> appealRecipe(@RequestParam("sign") String sign) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        String key = MAP_RECIPE_ID_KEY + sign;

        // 从Redis中获取sign对应的映射
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            return ResponseResult.fail("sign无效");
        }

        // 获取recipeId
        Long recipeId = Long.valueOf(s);

        // 根据userId和recipeId获取菜谱，真实目的是检查RecipeId和userId是否匹配
        Recipe recipe = recipeService.getRecipeByUserIdAndRecipeId(userId, recipeId);

        /*
         * 添加到Redis的菜谱上述Zset集合中，value为菜谱id，score为当前时间戳
         */
        stringRedisTemplate.opsForZSet().addIfAbsent(APPEAL_RECIPE_KEY, String.valueOf(recipe.getId()), System.currentTimeMillis());

        // 删除sign映射
        stringRedisTemplate.delete(key);

        return ResponseResult.success();
    }


    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱<br>
     * 仅限服务间调用
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/ban")
    public ResponseResult<String> setInAccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO) {
        Long recipeId = resultDTO.getRecipeId();
        Recipe recipe = recipeService.getById(recipeId);
        Long userId = recipe.getUserId();
        Integer censorState = resultDTO.getCensorState();

        // 更新审核状态
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

        /*
         * 调用消息模块，通知用户
         */
        censorService.sendViolationNotification(userId, recipeId, censorState, censorResult);

        return ResponseResult.success();
    }

    /**
     * 放行菜谱，人工审核或者机器审核通过时调用<br>
     * 仅限服务间调用
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/allow")
    public ResponseResult<String> setaccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO) {
        Integer censorState = resultDTO.getCensorState();
        Long recipeId = resultDTO.getRecipeId();


        // 根据recipeId获取当前菜谱
        Recipe recipe = recipeService.getById(recipeId);
        Long userId = recipe.getUserId();

        recipeService.updateCensorState(recipeId, censorState, 0);


        // 将审核结果保存
        String censorResult = resultDTO.getCensorResult();
        Long censorId = resultDTO.getCensorId();
        LocalDateTime censorTime = resultDTO.getCensorTime();
        censorService.saveCensorResult(recipeId, censorResult, censorId, censorTime, censorState);

        /*
         * 调用消息模块，通知用户
         */
        if (resultDTO.getIsNotify() == 1) {
            censorService.sendApprovalNotification(userId, recipeId);
        }

        return ResponseResult.success();
    }


}
