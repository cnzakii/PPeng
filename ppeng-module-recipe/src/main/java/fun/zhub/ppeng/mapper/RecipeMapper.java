package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

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


    /**
     * 根据id和时间，查询个数限制查询
     *
     * @param field    id字段
     * @param id       id
     * @param dateTime 限制时间
     * @param size     限制个数
     * @return list
     */
    List<Recipe> getRecipeListByIdAndTimeLimit(@Param("field") String field,@Param("id") Object id,@Param("dateTime") Timestamp dateTime,@Param("size") Integer size);
}
