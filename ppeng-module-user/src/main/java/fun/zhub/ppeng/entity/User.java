package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表，包含了用户的基本信息：用户id，手机号，昵称，头像地址等
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 4339224709741456224L;
    /**
     * 手机号登录用户：雪花算法唯一ID
     * 微信登录用户：openid
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Id
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 微信用户唯一id
     */
    private String openId;

    /**
     * 密码（加密方式：SM3 盐值：id）
     */
    private String password;

    /**
     * 昵称，默认是用户id前20位
     */
    private String nickName;

    /**
     * 头像路径
     */

    private String icon;

    /**
     * 用户角色：user
     */

    private String role;

    /**
     * 创建时间
     */

    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0", delval = "1")
    private Byte isDeleted;
}
