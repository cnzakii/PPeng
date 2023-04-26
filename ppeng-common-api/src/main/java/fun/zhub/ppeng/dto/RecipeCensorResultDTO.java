package fun.zhub.ppeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜谱审核结果数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeCensorResultDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 8622932682207036703L;

    /**
     * 菜谱id
     */
    private Long recipeId;

    /**
     * 审核类型 0 未审核  1 机器审核  2 人工审核   3 人工复审
     */
    private Integer censorState;


    /**
     * 审查结果
     */
    private String censorResult;

    /**
     * 审查人员id-如果是人工审核才填写
     */
    private Long censorId;

    /**
     * 审查时间
     */
    private LocalDateTime censorTime;




}
