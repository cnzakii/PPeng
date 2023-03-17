package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 审核被举报的评论表  
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_review_comment")
public class ReviewComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 管理员id
     */
    private Long adminId;

    /**
     * 审核结果：0：拒绝，1：通过
     */
    private Byte result;

    /**
     * 拒绝理由：200字以内
     */
    private String reason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
