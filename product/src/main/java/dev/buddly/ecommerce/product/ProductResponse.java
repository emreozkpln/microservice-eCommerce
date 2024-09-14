package dev.buddly.ecommerce.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Integer id,
        String productName,
        String description,
        BigDecimal price,
        int stock,
        List<String> imageUrl
) {
}
