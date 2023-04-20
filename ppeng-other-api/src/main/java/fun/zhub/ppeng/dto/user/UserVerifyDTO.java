package fun.zhub.ppeng.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *  用户修改敏感信息的邮箱验证请求
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-19
 **/
@Data
public class UserVerifyDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -7209715709339446395L;

    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotEmpty(message = "email不能为空")
    @Email(message = "邮箱格式错误")
    private String email;


    /*
     * 0 更新密码
     * 1 更新邮箱
     * 2 删除账号
     */
    @Max(value = 2, message = "type无效")
    @Min(value = 0, message = "type无效")
    private Integer type;
}
