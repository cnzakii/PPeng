package fun.zhub.ppeng.dto;



import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息更新 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-13
 **/
@Data
public class MessageUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7773479228318226059L;

    /**
     * 用户id
     */
    @MatchToken
    Long userId;

    /**
     * 消息id集合
     */
    @NotNull(message = "messageIds不能为空")
    Integer[] messageIds;
}
