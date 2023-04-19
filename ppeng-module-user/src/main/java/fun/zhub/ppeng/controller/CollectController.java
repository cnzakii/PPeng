package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.CollectService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 收藏菜谱表 前端控制器
 * </p>
 *
 * @author lbl
 * @since 2023-04-17
 */
@RestController
@RequestMapping("/user/collect")
public class CollectController {
    @Resource
    private CollectService collectService;

    /**
     * 收藏菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/add/{recipeId}")
    public ResponseResult<String> addcollectedRecipe(@PathVariable("recipeId") Long recipeId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        collectService.addCollectedRecipe(userId, recipeId);

        return ResponseResult.success();
    }

    /**
     * 列出该用户收藏菜谱的id列表
     *
     * @return set
     */
    @GetMapping("/list")
    public ResponseResult<Set<String>> queryMycollectedSet() {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        Set<String> set = collectService.queryCollectedRecipeSet(userId);

        return ResponseResult.success(set);
    }

    /**
     * 查看该用户是否收藏该菜谱
     *
     * @param recipeId 菜谱id
     * @return true or false
     */
    @PostMapping("/{recipeId}")
    public ResponseResult<Boolean> iscollect(@PathVariable("recipeId") Long recipeId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        Boolean liked = collectService.isCollected(userId, recipeId);

        return ResponseResult.success(liked);

    }

    /**
     * 取消收藏菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @DeleteMapping("/delete/{recipeId}")
    public ResponseResult<String> deletecollectedRecipe(@PathVariable("recipeId") Long recipeId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        collectService.deleteCollectedRecipe(userId, recipeId);

        return ResponseResult.success();
    }
}
