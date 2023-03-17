package fun.zhub.ppeng.mapper;

import fun.zhub.ppeng.entity.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 点赞菜谱表 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {
}
