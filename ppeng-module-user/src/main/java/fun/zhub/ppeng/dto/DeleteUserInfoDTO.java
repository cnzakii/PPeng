package fun.zhub.ppeng.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
/**
 * <p>
 *  用户具体信息删除
 * <p>
 *
 * @author lbl
 * @version 1.0
 * @since 2023-04-02
 **/
@Data
public class DeleteUserInfoDTO {
    @NotNull(message = "userId不能为空")
    private Long userId;
}
