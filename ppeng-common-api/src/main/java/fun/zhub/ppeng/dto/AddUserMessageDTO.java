package fun.zhub.ppeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 添加用户消息 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddUserMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4693880587758897919L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;


}
