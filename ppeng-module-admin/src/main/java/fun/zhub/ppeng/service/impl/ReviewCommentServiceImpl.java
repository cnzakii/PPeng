package fun.zhub.ppeng.service.impl;

import fun.zhub.ppeng.entity.ReviewComment;
import fun.zhub.ppeng.mapper.ReviewCommentMapper;
import fun.zhub.ppeng.service.ReviewCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审核被举报的评论表   服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class ReviewCommentServiceImpl extends ServiceImpl<ReviewCommentMapper, ReviewComment> implements ReviewCommentService {

}
