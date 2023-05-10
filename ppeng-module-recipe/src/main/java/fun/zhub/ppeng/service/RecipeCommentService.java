package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.RecipeComment;

import java.util.List;

/**
 * <p>
 * 菜谱评论表 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface RecipeCommentService extends IService<RecipeComment> {

    /**
     * 新增评论
     *
     * @param recipeId
     * @param parentId
     * @param commenterId
     * @param content
     */
    void addComment(Long recipeId, Long parentId, Long commenterId, String content);

    /**
     * 通过菜谱id删除评论
     *
     * @param id
     */
    void deleteCommentById(Long id);

    /**
     * 通过菜谱id获取评论
     *
     * @param recipeId
     * @return
     */
    List<RecipeComment> getCommentsByRecipeId(Long recipeId);

    /**
     * 创建子评论
     *
     * @param recipeComment
     */
    void setChildren(RecipeComment recipeComment);




}
