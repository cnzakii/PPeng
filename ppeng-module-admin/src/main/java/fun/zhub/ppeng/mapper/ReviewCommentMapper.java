package fun.zhub.ppeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.zhub.ppeng.entity.ReviewComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 审核被举报的评论表   Mapper 接口
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Mapper
public interface ReviewCommentMapper extends BaseMapper<ReviewComment> {

}
