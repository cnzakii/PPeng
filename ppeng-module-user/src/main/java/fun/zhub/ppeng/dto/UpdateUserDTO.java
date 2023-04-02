package fun.zhub.ppeng.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 *  用户基本信息更新
 * <p>
 *
 * @author lbl
 * @version 1.0
 * @since 2023-04-02
 **/
@Data
public class UpdateUserDTO implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    @NotNull(message = "id不能为空")
    private Long id;
    @NotEmpty(message = "phone不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "手机号格式错误")
    private String phone;
    @NotEmpty(message = "nickName不能为空")
    private String nickName;
    @NotEmpty(message = "icon不能为空")
    private String icon;

}
