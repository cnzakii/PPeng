package fun.zhub.ppeng.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户注册结构体
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-07
 **/
@Data
public class RegisterDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;


    @Email( message = "邮箱格式错误")
    private String email;


    @Pattern(regexp = "^\\d{6}$", message = "验证码无效")
    private String code;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
