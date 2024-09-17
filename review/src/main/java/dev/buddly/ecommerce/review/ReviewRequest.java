package dev.buddly.ecommerce.review;

public record ReviewRequest(
        String id,
        String comment,
        double rating
        //Integer userId
) {
}
