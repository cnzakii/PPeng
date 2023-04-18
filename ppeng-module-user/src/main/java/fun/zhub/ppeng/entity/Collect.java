package fun.zhub.ppeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 收藏菜谱表
 * </p>
 *
 * @author lbl
 * @since 2023-04-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_collect")
public class Collect {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收藏的菜谱id
     */
    private Long recipeId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
