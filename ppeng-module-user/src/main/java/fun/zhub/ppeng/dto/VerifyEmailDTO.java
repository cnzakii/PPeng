package fun.zhub.ppeng.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 验证用户邮箱
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-19
 **/
@Data
public class VerifyEmailDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2435520170423887907L;

    /**
     * 用户id
     */
    @NotEmpty(message = "userId不能为空")
    private Long userId;


    /**
     * email
     */
    @NotEmpty(message = "email不能为空")
    @Email(message = "邮箱格式错误")
    private String email;


    /**
     * 验证码
     */
    @Pattern(regexp = "^\\d{6}$", message = "验证码无效")
    private String code;


    /*
     * 0 更新密码
     * 1 更新邮箱
     * 2 删除账号
     */
    @Max(value = 2, message = "type无效")
    @Min(value = 0, message = "type无效")
    private Integer type;
}
