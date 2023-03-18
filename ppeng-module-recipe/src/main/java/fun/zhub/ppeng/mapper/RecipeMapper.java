package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {

}
