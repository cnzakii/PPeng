package fun.zhub.ppeng.entity;


import fun.zhub.ppeng.constant.AnalyzerType;
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
     * 合并标题和内容
     */
    @Field(type = FieldType.Text, analyzer = AnalyzerType.IK_MAX_WORD, searchAnalyzer = AnalyzerType.IK_SMART)
    private String combind;

    /**
     * 菜谱id
     */
    @Id
    private Long id;

    /**
     * 发布者id
     */
    @Field
    private Long userId;

    /**
     * 菜谱类型
     */
    @Field
    private Integer typeId;

    /**
     * 标题：现在150字以内
     */
    @Field(type = FieldType.Text, analyzer = AnalyzerType.IK_MAX_WORD, searchAnalyzer = AnalyzerType.IK_SMART, copyTo = {"combind"})
    private String title;

    /**
     * 配料表
     */
    @Field
    private String material;

    /**
     * 文字内容
     */
    @Field(type = FieldType.Text, analyzer = AnalyzerType.IK_MAX_WORD, searchAnalyzer = AnalyzerType.IK_SMART, copyTo = {"combind"})
    private String content;

    /**
     * 图片路径
     */
    @Field
    private String mediaUrl;

    /**
     * 0代表图文，1代表视频
     */
    @Field
    private Integer isVideo;

    /**
     * 0代表非专业，1代表是专业
     */
    @Field
    private Integer isProfessional;

    /**
     * 点赞数
     */
    @Field
    private Integer likes;

    /**
     * 收藏数
     */
    @Field
    private Integer collections;

    /**
     * 创建时间
     */
    @Field
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
