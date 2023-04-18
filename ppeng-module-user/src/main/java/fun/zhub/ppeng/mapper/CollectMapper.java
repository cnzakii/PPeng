package fun.zhub.ppeng.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.Collect;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 收藏菜谱表 Mapper 接口
 * </p>
 *
 * @author lbl
 * @since 2023-04-17
 */
@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
}

