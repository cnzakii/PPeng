package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号
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
     * 用户角色：admin，superAdmin
     */
    private String role;

    /**
     * 已处理任务数
     */
    private Integer processed;

    /**
     * 审核通过数
     */
    private Integer passed;

    /**
     * 审核未通过数
     */
    private Integer rejected;

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
