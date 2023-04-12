package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.LikeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 点赞菜谱表 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    /**
     * 添加喜欢的菜谱
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/add/{recipeId}")
    public ResponseResult<String> AddFavoriteRecipe(@PathVariable("recipeId")Long recipeId ){
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        likeService.addLikedRecipe(userId,recipeId);


        return ResponseResult.success();
    }



}
