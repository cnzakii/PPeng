package fun.zhub.ppeng.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜谱审核 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@Data
public class RecipeCensorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7970254078767106001L;


    /**
     * 菜谱id
     */
    private Long recipeId;

    /**
     * 机器审核结果
     */
    private String autoCensorResult;

    /**
     * 机器审核时间
     */
    private LocalDateTime autoCensorTime;

    /**
     * 第一次人工审查结果，没有则为null
     */
    private String manualCensorResult;

    /**
     * 人工审查时间
     */
    private LocalDateTime manualCensorTime;
}
