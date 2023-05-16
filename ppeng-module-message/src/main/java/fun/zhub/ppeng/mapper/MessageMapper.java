package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 用户消息表 Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-05-13
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 根据用户id和时间，查询个数限制查询
     *
     * @param userId   用户id
     * @param dateTime 限制时间
     * @param size     限制查询个数
     * @return list
     */
    List<Message> selectMessageListByIdAndTimeLimit(@Param("userId") Long userId, @Param("dateTime") Timestamp dateTime, @Param("size") Integer size);

}
