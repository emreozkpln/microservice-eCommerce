package dev.buddly.ecommerce.review;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "review-service",
        url = "${application.config.review-url}"
)
public interface ReviewClient {
    @GetMapping("/id/{reviewId}")
    ReviewResponse getReviewById(
            @PathVariable String reviewId
    );
}
