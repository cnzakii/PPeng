package fun.zhub.ppeng.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Recipe 数据传输对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-04
 **/
@Data
public class RecipeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 441911966994353992L;

    /**
     * 菜谱id
     */
    private Long id;

    /**
     * 发布者id
     */
    private Long userId;

    /**
     * 菜谱类型
     */
    private Integer typeId;

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
     * 点赞数
     */
    private Integer likes;

    /**
     * 收藏数
     */
    private Integer collections;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
