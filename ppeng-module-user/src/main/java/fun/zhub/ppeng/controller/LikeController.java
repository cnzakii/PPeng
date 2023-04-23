package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.LikeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 点赞菜谱表 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/user/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    /**
     * 添加喜欢的菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/add/{recipeId}")
    public ResponseResult<String> addLikedRecipe(@PathVariable("recipeId") Long recipeId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        likeService.addLikedRecipe(userId, recipeId);


        return ResponseResult.success();
    }


    /**
     * 列出该用户点赞菜谱的id列表
     *
     * @return set
     */
    @GetMapping("/list")
    public ResponseResult<Set<String>> queryMyLikedSet() {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        Set<String> set = likeService.queryLikedRecipeSet(userId);

        return ResponseResult.success(set);
    }

    /**
     * 查看该用户是否点赞该菜谱
     *
     * @param recipeId 菜谱id
     * @return true or false
     */
    @PostMapping("/{recipeId}")
    public ResponseResult<Boolean> isLike(@PathVariable("recipeId") Long recipeId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        Boolean liked = likeService.isLiked(userId, recipeId);

        return ResponseResult.success(liked);
    }


    /**
     * 取消点赞菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @DeleteMapping("/delete/{recipeId}")
    public ResponseResult<String> deleteLikedRecipe(@PathVariable("recipeId") Long recipeId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        likeService.deleteLikedRecipe(userId, recipeId);

        return ResponseResult.success();
    }


}
