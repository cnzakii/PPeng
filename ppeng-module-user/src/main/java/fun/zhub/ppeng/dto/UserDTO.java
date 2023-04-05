package fun.zhub.ppeng.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;


    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户手机号，脱敏处理
     */
    private String phone;


    /**
     * 昵称，默认是用户id前20位
     */
    private String nickName;

    /**
     * 头像路径
     */
    private String icon;

}
