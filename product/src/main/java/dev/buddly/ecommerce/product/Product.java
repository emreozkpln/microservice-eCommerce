package dev.buddly.ecommerce.product;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String product_name;
    private String description;
    private BigDecimal price;
    private int stock;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrl;
    @ElementCollection
    private Map<String,Double> comments;
}
