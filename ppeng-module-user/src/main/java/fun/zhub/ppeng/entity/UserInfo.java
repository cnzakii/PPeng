package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表，包含更加详细的用户信息：所在城市，自我介绍，性别，生日 等
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户所在城市
     */
    private String city;

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
     * 用户性别，0：女，1：男
     */
    private Byte gender;

    /**
     * 用户生日
     */
    private LocalDate birthday;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
