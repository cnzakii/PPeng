package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName("t_recipe_comments")
public class RecipeComments implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 点赞数
     */
    private Integer likes;

    /**
     * 状态，0：正常，1：被举报 ，2：禁止查看
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
