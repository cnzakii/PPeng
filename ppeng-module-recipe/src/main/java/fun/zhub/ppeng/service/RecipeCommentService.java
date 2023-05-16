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
     * @param recipeId    菜谱id
     * @param parentId    父类评论id
     * @param commenterId 评论者id
     * @param content     内容
     */
    void addComment(Long recipeId, Integer parentId, Long commenterId, String content);

    /**
     * 通过评论id删除评论
     *
     * @param id     评论id
     * @param userId 用户id
     */
    void deleteCommentById(Integer id, Long userId);

    /**
     * 通过菜谱id获取评论
     *
     * @param recipeId 菜谱id
     * @return list
     */
    List<RecipeComment> getCommentsByRecipeId(Long recipeId);

    /**
     * 创建子评论
     *
     * @param recipeComment ecipeComment
     */
    void setChildren(RecipeComment recipeComment);


}
