package fun.zhub.ppeng.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 推荐菜谱数据传输对象
 *
 * @author Zaki
 * @version 1。0
 * @since 2023-05-08
 **/
@Data
public class RecommendRecipeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5151962095149191569L;

    private List<RecipeDTO> recipeDTOList;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long minTimestamp;
}
