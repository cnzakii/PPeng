package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜谱评论表
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_recipe_comment")
public class RecipeComment implements Serializable {


    @Serial
    private static final long serialVersionUID = -1111505832798447786L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜谱id
     */
    private Long recipeId;

    /**
     * 关联一级评论id，如果是一级评论，则值为0
     */
    private Integer parentId;

    /**
     * 评论者id
     */
    private Long commenterId;

    /**
     * 评论内容，不超过150字
     */
    private String content;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0，未删除，1，已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;


    /**
     * 子评论
     */
    @TableField(exist = false)
    private List<RecipeComment> children;








}
