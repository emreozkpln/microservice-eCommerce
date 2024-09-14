package dev.buddly.ecommerce.product;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name should be not null")
        String productName,
        @NotNull(message = "Description should be not null")
        String description,
        @NotNull(message = "Price should be not null")
        BigDecimal price,
        @NotNull(message = "Stock should be not null")
        int stock,
        List<String> imageId
) {
}
