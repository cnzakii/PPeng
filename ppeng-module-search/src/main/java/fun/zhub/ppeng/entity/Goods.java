package fun.zhub.ppeng.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

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

    @Field
    private String title;

    @Field
    private BigDecimal price;

    @Field
    private String publishDate;
}