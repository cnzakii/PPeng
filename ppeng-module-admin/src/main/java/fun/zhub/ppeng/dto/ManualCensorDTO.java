package fun.zhub.ppeng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人工审查菜谱 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@Data
public class ManualCensorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1658564221291316344L;

    /**
     * 菜谱id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long recipeId;

    /**
     * 标题：现在150字以内
     */
    private String title;

    /**
     * 配料表
     */
    private String material;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 图片路径
     */
    private String mediaUrl;

    /**
     * 0代表图文，1代表视频
     */
    private Integer isVideo;

    /**
     * 机器审核结果
     */
    private String autoCensorResult;

    /**
     * 机器审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime autoCensorTime;

    /**
     * 人工审查结果
     */
    private String manualCensorResult;


    /**
     * 人工审查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime manualCensorTime;


}
