package fun.zhub.ppeng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * message 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-13
 **/
@Data
public class MessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1162765098566607193L;

    /**
     * 消息id
     */
    private Integer id;


    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态：0未读，1已读
     */
    private Integer status;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;


}
