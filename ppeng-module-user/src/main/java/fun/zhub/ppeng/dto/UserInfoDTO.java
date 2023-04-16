package fun.zhub.ppeng.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户基本信息
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
@Data
public class UserInfoDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 4619547060089446815L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像url
     */
    private String icon;

    /**
     * 用户地址
     */
    private String[] address;


    /**
     * 简介
     */
    private String introduce;

    /**
     * 性别
     */
    private Byte gender;

    /**
     * 生日
     */
    private LocalDate birthday;


    /**
     * 用户粉丝数
     */
    private Integer fans;

    /**
     * 用户关注数
     */
    private Integer follows;


    /**
     * 创建时间
     */

    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
