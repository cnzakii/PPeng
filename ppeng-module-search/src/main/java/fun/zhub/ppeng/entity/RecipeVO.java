package fun.zhub.ppeng.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static fun.zhub.ppeng.constant.AnalyzerType.IK_MAX_WORD;


/**
 * 存入ElasticSearch的菜谱对象
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "recipe")
public class RecipeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -656244681426117701L;

    /**
     * 菜谱id
     */
    @Id
    private Long id;

    /**
     * 发布者id
     */
    @Field(type = FieldType.Keyword, analyzer =IK_MAX_WORD, searchAnalyzer = "ik_smart")
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
