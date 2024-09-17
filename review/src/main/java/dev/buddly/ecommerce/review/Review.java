package dev.buddly.ecommerce.review;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id
    private String id;
    private String comment;
    private double rating;
    //private Integer userId;
}
