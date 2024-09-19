package dev.buddly.ecommerce.elasticsearch;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Document(indexName = "products_index")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

    @Id
    private String id;
    @Field(name = "product_name",type = FieldType.Text)
    private String product_name;
    @Field(name = "description",type = FieldType.Text)
    private String description;
    @Field(name = "price",type = FieldType.Double)
    private BigDecimal price;
    @Field(name = "stock",type = FieldType.Integer)
    private int stock;
    @ElementCollection(fetch = FetchType.EAGER)
    @Field(name = "imageUrl", type = FieldType.Text)
    private List<String> imageUrl;
    @ElementCollection
    @Field(name = "comments", type = FieldType.Object)
    private Map<String,Double> comments;

}
