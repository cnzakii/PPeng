package fun.zhub.ppeng.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜谱类型数据传输对象
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-23
 **/
@Data
public class RecipeTypeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4182040393251012760L;

    /**
     * id
     */
    private Integer id;

    /**
     * 类型
     */
    private String name;

    /**
     * 父类id，顶级分类为0
     */
    private Integer parentId;
}
