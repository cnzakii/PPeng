package fun.zhub.ppeng.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户登录结构体
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/

@Data
public class LoginFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;


    @Email(message = "邮箱格式错误")
    private String email;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
