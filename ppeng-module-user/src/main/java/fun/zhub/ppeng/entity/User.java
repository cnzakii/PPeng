package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
     * 用户所在地区
     */
    private String address;

    /**
     * 用户简介，不超过128个字符
     */
    private String introduce;

    /**
     * 用户粉丝数
     */
    private Integer fans;

    /**
     * 用户关注数
     */
    private Integer follows;

    /**
     * 用户性别，0：未指定，1：女，3：男
     */
    private Integer gender;

    /**
     * 用户生日
     */
    private LocalDate birthday;


    /**
     * 用户角色：user
     */

    private String role;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
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
    private Integer isDeleted;
}
