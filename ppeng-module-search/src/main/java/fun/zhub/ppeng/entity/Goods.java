package fun.zhub.ppeng.entity;

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

    @Field
    private String name;

    @Field(type = FieldType.Long,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;

    @Field
    private BigDecimal price;

    @Field
    private String publishDate;
}