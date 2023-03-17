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
 * 用户表，包含了用户的基本信息：用户id，手机号，昵称，头像地址等
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号登录用户：雪花算法唯一ID
微信登录用户：openid
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户手机号
     */
    private String phone;

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
    private Byte isDeleted;
}
