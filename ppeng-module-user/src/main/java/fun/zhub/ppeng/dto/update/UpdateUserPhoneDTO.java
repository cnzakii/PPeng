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
public class UpdateUserPhoneDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户id
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 旧手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "手机号格式错误")
    private String oldPhone;

    /**
     * 旧手机验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String oldCode;

    /**
     * 新手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "手机号格式错误")
    private String newPhone;

    /**
     * 新手机验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String newCode;


}
