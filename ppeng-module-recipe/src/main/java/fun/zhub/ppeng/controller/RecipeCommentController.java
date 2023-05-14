package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCommentDTO;
import fun.zhub.ppeng.entity.RecipeComment;
import fun.zhub.ppeng.service.RecipeCommentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     * @param recipeCommentDTO recipeCommentDTO
     * @return success
     */
    @PostMapping("/push")
    public ResponseResult<String> pushComment(@Valid @RequestBody RecipeCommentDTO recipeCommentDTO) {

        Long commenterId = recipeCommentDTO.getCommenterId();

        Long recipeId = recipeCommentDTO.getRecipeId();
        Integer parentId = recipeCommentDTO.getParentId();
        String content = recipeCommentDTO.getContent();
        recipeCommentService.addComment(recipeId, parentId, commenterId, content);

        /*
         *TODO 审核菜谱评论content
         */

        return ResponseResult.success("评论成功");
    }

    /**
     * 通过评论id删除评论
     *
     * @param id id
     * @return success
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult<String> deleteComment(@PathVariable("id") Integer id) {
       // Long userId = Long.valueOf((String) StpUtil.getLoginId());
        /*
         * TODO 验证身份，只有评论者和作者能够删除
         */

        recipeCommentService.deleteCommentById(id);
        return ResponseResult.success("删除成功");

    }


    /**
     * 通过菜谱id查看评论
     *
     * @param recipeId recipeId
     * @return list
     */
    @GetMapping("/list/{recipeId}")
    public ResponseResult<List<RecipeComment>> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId) {

        var commentList = recipeCommentService.getCommentsByRecipeId(recipeId);
        return ResponseResult.success(commentList);
    }


}
