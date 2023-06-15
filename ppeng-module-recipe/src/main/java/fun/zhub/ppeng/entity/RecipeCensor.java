package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Zaki
 * @since 2023-04-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_recipe_censor")
public class RecipeCensor implements Serializable {


    @Serial
    private static final long serialVersionUID = 8510620607375674738L;
    /**
     * 菜谱id
     */
    @TableId(value = "recipe_id")
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
     * 人工审查结果
     */
    private String manualCensorResult;

    /**
     * 第一次人工审查人员id
     */
    private Long manualCensorId;

    /**
     * 人工审查时间
     */
    private LocalDateTime manualCensorTime;

    /**
     * 第二次人工审核结果
     */
    private String secondManualCensorResult;

    /**
     * 第二次人工审核人员id
     */
    private Long secondManualCensorId;

    /**
     * 第二次人工审核时间
     */
    private LocalDateTime secondManualCensorTime;
}
