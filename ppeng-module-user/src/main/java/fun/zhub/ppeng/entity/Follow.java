package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户关注表
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_follow")
public class Follow implements Serializable {

    @Serial
    private static final long serialVersionUID = -4077818067684656827L;
    /**
     * 自增id
     */
    @TableId(value = "fid", type = IdType.AUTO)
    private Integer fid;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关注的人的id
     */
    private Long followId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
