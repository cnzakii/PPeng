package fun.zhub.ppeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 文本审核传输对象
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@Data
@NoArgsConstructor
public class ContentCensorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3246666969807229076L;

    /**
     * 审核类型：昵称，菜谱内容....
     */
    private String type;

    /**
     * 用户id 或者 菜谱id
     */
    private Long id;

    /**
     * 审核数据
     */
    private String[] data;

    public void setData(String... data) {
        this.data = data;
    }

    public ContentCensorDTO(String type, Long id, String... data) {
        this.type = type;
        this.id = id;
        this.data = data;
    }
}
