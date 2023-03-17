package fun.zhub.ppeng.mapper;

import fun.zhub.ppeng.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}
