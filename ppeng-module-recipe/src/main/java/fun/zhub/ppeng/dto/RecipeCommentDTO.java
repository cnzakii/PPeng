package fun.zhub.ppeng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author boliang
 */
@Data
public class RecipeCommentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7972880728057882121L;

    /**
     * 菜谱id
     */
    @NotNull(message = "recipeId不能为空")
    private Long recipeId;

    /**
     * 关联一级评论id，如果是一级评论，则值为0
     */
    @NotNull(message = "parentId不能为空")
    private Integer parentId;

    /**
     * 评论者id
     */
    @NotNull(message = "commenterId不能为空")
    @MatchToken
    private Long commenterId;

    /**
     * 评论内容，不超过150字
     */
    @NotEmpty(message = "评论不能为空")
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
