package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户消息表 实体类
 *
 * @author Zaki
 * @since 2023-05-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_message")
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 4338007562824282244L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态：0未读，1已读
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
