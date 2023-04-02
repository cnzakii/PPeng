package fun.zhub.ppeng.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  用户基本信息删除
 * <p>
 *
 * @author lbl
 * @version 1.0
 * @since 2023-04-02
 **/
@Data
public class DeleteUserDTO implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    @NotNull(message = "id不能为空")
    private Long id;


}
