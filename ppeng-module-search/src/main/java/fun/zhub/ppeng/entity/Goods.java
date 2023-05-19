package fun.zhub.ppeng.entity;

import fun.zhub.ppeng.constant.AnalyzerType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "goods")
public class Goods implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = AnalyzerType.IK_MAX_WORD, searchAnalyzer = AnalyzerType.IK_SMART, copyTo = {"combind"})
    private String name;

    @Field(type = FieldType.Text, analyzer = AnalyzerType.IK_MAX_WORD, searchAnalyzer = AnalyzerType.IK_SMART, copyTo = {"combind"})
    private String title;

    @Field
    private BigDecimal price;

    @Field
    private String publishDate;

    @Field(type = FieldType.Text, analyzer = AnalyzerType.IK_MAX_WORD, searchAnalyzer = AnalyzerType.IK_SMART, ignoreFields = {"combined"}, excludeFromSource = true)
    private String combined;
}