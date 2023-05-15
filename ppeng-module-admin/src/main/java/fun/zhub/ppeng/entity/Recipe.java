package fun.zhub.ppeng.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Data
public class Recipe implements Serializable {


    @Serial
    private static final long serialVersionUID = -1966291447312406958L;

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
     * 0代表非专业，1代表是专业
     */
    private Integer isProfessional;

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

    /**
     * 审核状态： 0 未审核，1 机器审核，2 人工审核，3 人工复审
     */
    private Integer censorState;

    /**
     * 违规状态： 0 未违规， 1 违规
     */
    private Integer isBaned;


    /**
     * 逻辑删除:审核通过，但用户自己删除
     */
    private Integer isDeleted;
}
