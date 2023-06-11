package fun.zhub.ppeng.dto;

import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新菜谱 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-08
 **/
@Data
public class UpdateRecipeDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 7759403294958768269L;


    /**
     * 发布者id
     */
    @NotNull(message = "userId不能为空")
    @MatchToken
    private Long userId;

    /**
     * 菜谱id
     */
    @NotNull(message = "recipeId不能为空")
    private Long recipeId;

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

}
