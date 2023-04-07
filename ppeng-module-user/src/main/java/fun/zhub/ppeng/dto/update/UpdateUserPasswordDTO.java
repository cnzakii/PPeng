package fun.zhub.ppeng.dto.update;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 更新用户密码
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-04
 **/
@Data
public class UpdateUserPasswordDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户id
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 邮箱
     */
    @NotEmpty(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    private String email;


    /**
     * 手机验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String verifyCode;

    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "密码太弱")
    private String newPassword;

}
