package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据用户ID来更新用户的粉丝数、关注数
     *
     * @param userId 用户Id
     * @param field  更新的字段
     * @param change 更新的数值
     */
    int updateUserStatsById(@Param("userId") Long userId,@Param("field") String field,@Param("change") int change);
}
