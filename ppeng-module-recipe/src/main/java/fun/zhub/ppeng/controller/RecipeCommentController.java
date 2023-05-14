package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.PushRecipeDTO;
import fun.zhub.ppeng.dto.RecipeCommentDto;
import fun.zhub.ppeng.entity.RecipeComment;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.RecipeCommentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 菜谱评论表 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/recipe/comment")
public class RecipeCommentController {
    @Resource
    private RecipeCommentService recipeCommentService;

    /**
     * 发表评论
     * @param recipeCommentDto
     * @return
     */
    @PostMapping("/push")
    public ResponseResult<String> pushComment(@Valid @RequestBody RecipeCommentDto recipeCommentDto) {
        // 验证id是否和token对应的id一致
        // Long id = Long.valueOf((String) StpUtil.getLoginId());
        Long commenterId = recipeCommentDto.getCommenterId();
        // if (!Objects.equals(id, commenterId)) {
        // return ResponseResult.fail("id错误");
        //}

        Long recipeId = recipeCommentDto.getRecipeId();
        Long parentId = recipeCommentDto.getParentId();
        //TODO 审核菜谱评论content
        String content = recipeCommentDto.getContent();
        recipeCommentService.addComment(recipeId, parentId, commenterId, content);
        return ResponseResult.success("评论成功");
    }

    /**
     * 通过评论id删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult<String> deleteComment(@PathVariable("id") Long id) {
        recipeCommentService.deleteCommentById(id);
        return ResponseResult.success("删除成功");

    }


    /**
     * 通过菜谱id查看评论
     * @param recipeId
     * @return
     */
    @GetMapping("/list/{recipeId}")
    public List<RecipeComment> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId) {
        return recipeCommentService.getCommentsByRecipeId(recipeId);
    }


}
