package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表，包含了用户的基本信息：用户id，手机号，昵称，头像地址等 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
