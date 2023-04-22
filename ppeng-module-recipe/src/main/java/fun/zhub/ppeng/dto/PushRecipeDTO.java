package fun.zhub.ppeng.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 菜谱发布请求
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-22
 **/
@Data
public class PushRecipeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7972880728057882121L;


    /**
     * 发布者id
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 菜谱类型
     */
    @NotNull(message = "菜谱类型不能为空")
    private Integer typeId;

    /**
     * 标题：现在150字以内
     */
    @NotEmpty(message = "标题不能为空")
    private String title;

    /**
     * 配料表
     */
    @NotEmpty(message = "配料表不能为空")
    private String[] material;


    /**
     * 文字内容
     */
    private String content;

    /**
     * 图片路径
     */
    private String[] imges;

    /**
     * 视频路径
     */
    private String video;

    /**
     * 0代表图文，1代表视频
     */
    @Range(min = 0, max = 1, message = "isVideo标识错误")
    private Integer isVideo;

    /**
     * 0代表非专业，1代表是专业
     */
    @Range(min = 0, max = 1, message = "isProfessional标识错误")
    private Integer isProfessional;
}
