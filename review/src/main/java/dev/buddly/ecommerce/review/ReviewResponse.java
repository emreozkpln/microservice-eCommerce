package dev.buddly.ecommerce.review;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ReviewResponse(
        String id,

        @NotNull(message = "Comment cannot be null")
        String comment,

        @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
        @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
        double rating

        //Integer userId
) {
}
