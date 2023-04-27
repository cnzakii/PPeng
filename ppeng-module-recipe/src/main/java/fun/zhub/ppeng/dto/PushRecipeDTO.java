package fun.zhub.ppeng.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
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
     * 标题：200字以内
     */
    @NotEmpty(message = "标题不能为空")
    private String title;

    /**
     * 配料表
     */
    @NotEmpty(message = "配料表不能为空")
    private String material;

    /**
     * 文字内容
     */
    @Length(max = 400, message = "文字信息过长")
    private String content;

    /**
     * 媒体文件路径
     */
    @NotNull(message = "资源路径不能为空")
    private String[] mediaUrl;


    /**
     * 0代表图文，1代表视频
     */
    @Range(min = 0, max = 1, message = "isVideo标识错误")
    private Integer isVideo;


}
