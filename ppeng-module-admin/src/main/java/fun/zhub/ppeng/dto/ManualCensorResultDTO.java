package fun.zhub.ppeng.dto;

import fun.zhub.ppeng.validation.annotation.MatchToken;
import fun.zhub.ppeng.validation.annotation.ZeroOrOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 人工审核结果 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@Data
public class ManualCensorResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5923104579262599779L;

    /**
     * 管理员Id
     */
    @NotNull(message = "adminId不能为空")
    @MatchToken
    private Long adminId;

    /**
     * 菜谱id
     */
    @NotNull(message = "adminId不能为空")
    private Long recipeId;

    /**
     * 审核结果,只能是0或者1
     */
    @ZeroOrOne(message = "result标识错误")
    private Integer result;

    /**
     * 判定理由
     */
    @NotEmpty(message = "判定理由不能为空")
    private String reason;

}
