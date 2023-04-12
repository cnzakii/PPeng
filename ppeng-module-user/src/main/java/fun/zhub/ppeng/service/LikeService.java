package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.Like;

import java.util.Set;

/**
 * <p>
 * 点赞菜谱表 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface LikeService extends IService<Like> {

    /**
     * 添加喜欢的菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    void addLikedRecipe(Long userId, Long recipeId);


    /**
     * 查询该用户所点赞的菜谱列表
     * @param userId userId
     * @return Set
     */
    Set<String> queryLikedRecipeSet(Long userId);


    /**
     * 查询是否已经被点赞
     * @param userId 用户id
     * @param recipeId 菜谱
     * @return true or false
     */
    Boolean isLiked(Long userId, Long recipeId);
}
