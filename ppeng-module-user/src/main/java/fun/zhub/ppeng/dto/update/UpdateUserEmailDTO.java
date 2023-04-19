package fun.zhub.ppeng.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 更新用户手机号
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-05
 **/
@Data
public class UpdateUserEmailDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 2732738044211974759L;

    /**
     * 用户id
     */
    @NotNull(message = "userId不能为空")
    private Long userId;


    /**
     * 邮箱
     */
    @Email( message = "邮箱格式错误")
    private String email;

    /**
     * 验证码
     */
    @Pattern(regexp = "^\\d{6}$", message = "验证码无效")
    private String code;


}
