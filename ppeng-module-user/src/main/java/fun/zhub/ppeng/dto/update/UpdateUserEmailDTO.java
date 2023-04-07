package fun.zhub.ppeng.dto.update;

import jakarta.validation.constraints.NotEmpty;
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
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户id
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 旧邮箱
     */
    @NotEmpty(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    private String oldEmail;

    /**
     * 旧验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String oldCode;

    /**
     * 新邮箱
     */
    @NotEmpty(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    private String newEmail;

    /**
     * 新验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String newCode;


}
