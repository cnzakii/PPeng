package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.RecipeComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜谱评论表 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface RecipeCommentMapper extends BaseMapper<RecipeComment> {

    /**
     * 查根据父id询所有的评论信息
     * @param parentId
     * @return
     */
    List<RecipeComment> findByParentId(Long parentId);

    /**
     * 通过菜谱id查询该id下的所有parent_id=0的评论信息
     * @param parentId
     * @return
     */
    List<RecipeComment> findByRecipeId(@Param("recipeId") Long recipeId,@Param("parentId") Long parentId);


}
