package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.Collect;

import java.util.Set;


/**
 * <p>
 * 收藏菜谱表 服务类
 * </p>
 *
 * @author lbl
 * @since 2023-04-17
 */
public interface CollectService extends IService<Collect> {
    /**
     * 收藏菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    void addCollectedRecipe(Long userId, Long recipeId);


    /**
     * 查询该用户所收藏的菜谱列表
     *
     * @param userId 用户id
     * @return Set
     */
    Set<String> queryCollectedRecipeSet(Long userId);


    /**
     * 删除收藏的菜谱
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     */
    void deleteCollectedRecipe(Long userId, Long recipeId);


    /**
     * 查询是否已经被收藏
     *
     * @param userId   用户id
     * @param recipeId 菜谱id
     * @return true or false
     */
    Boolean isCollected(Long userId, Long recipeId);

}
