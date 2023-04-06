package fun.zhub.ppeng.dto.update;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 用户具体信息更新
 * <p>
 *
 * @author lbl
 * @version 1.0
 * @since 2023-04-02
 **/
@Data
public class UpdateUserInfoDTO implements Serializable {

    @NotNull(message = "userId不能为空")
    private Long userId;

    private String city;

    @Max(value = 128, message = "超出字数限制")
    private String introduce;

    private Byte gender;

    private LocalDate birthday;


}
