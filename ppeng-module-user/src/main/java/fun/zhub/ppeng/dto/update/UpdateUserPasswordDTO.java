package fun.zhub.ppeng.dto.update;

import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private static final long serialVersionUID = 7540380091268654051L;

    /**
     * 用户id
     */
    @NotNull(message = "userId不能为空")
    @MatchToken
    private Long userId;


    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

}
