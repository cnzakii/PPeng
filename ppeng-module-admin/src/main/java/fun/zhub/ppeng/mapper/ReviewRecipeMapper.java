package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.ReviewRecipe;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 审核菜谱表 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface ReviewRecipeMapper extends BaseMapper<ReviewRecipe> {

}
