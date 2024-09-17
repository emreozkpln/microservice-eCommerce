package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.review.ReviewResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Integer id,
        String productName,
        String description,
        BigDecimal price,
        int stock,
        List<String> imageUrl,
        List<ReviewResponse> comments
) {
}
