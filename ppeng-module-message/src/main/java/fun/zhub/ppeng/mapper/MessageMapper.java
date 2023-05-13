package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.Message;
import org.apache.ibatis.annotations.Mapper;

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

}
