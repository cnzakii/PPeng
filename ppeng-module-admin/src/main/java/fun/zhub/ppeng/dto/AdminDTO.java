package fun.zhub.ppeng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员信息数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-16
 **/
@Data
public class AdminDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2611606568241277016L;


    /**
     * 管理员id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 邮箱
     */
    private String email;



    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像路径
     */
    private String icon;


    /**
     * 已处理任务数
     */
    private Integer processed;

    /**
     * 审核通过数
     */
    private Integer passed;

    /**
     * 审核未通过数
     */
    private Integer rejected;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;

}
