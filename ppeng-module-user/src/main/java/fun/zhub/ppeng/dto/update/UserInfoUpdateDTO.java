package fun.zhub.ppeng.dto.update;




import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 用户具体信息更新
 * <p>
 *
 * @author lbl
 * @version 1.0
 * @since 2023-04-02
 **/
@Data
public class UserInfoUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4283608838082701437L;


    /**
     * userId
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 昵称
     */
    @Length(max = 20, message = "超出字数限制")
    private String nickName;

    /**
     * 头像url
     */
    private String icon;

    /**
     * 用户地址
     */
    private String[] address;

    @Length(max = 128, message = "超出字数限制")
    private String introduce;

    /**
     * 性别
     */

    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

}
