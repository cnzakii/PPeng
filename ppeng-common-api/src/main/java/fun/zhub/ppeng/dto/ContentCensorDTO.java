package fun.zhub.ppeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class ContentCensorDTO {

    /**
     * 审核类型：昵称，菜谱内容....
     */
    private String type;

    /**
     * 用户id 或者 菜谱id
     */
    private Long id;

    /**
     * 审核内容
     */
    private String content;
}
