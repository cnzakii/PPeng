package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.RecipeType;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜谱类型表 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface RecipeTypeMapper extends BaseMapper<RecipeType> {

}
