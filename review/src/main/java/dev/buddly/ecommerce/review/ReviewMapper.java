package dev.buddly.ecommerce.review;

import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {

    public Review toEntity(ReviewRequest request){
        return Review.builder()
                .id(request.id())
                .comment(request.comment())
                .rating(request.rating())
                .build();
    }

    public ReviewResponse toResponse(Review review){
        return new ReviewResponse(
                review.getId(),
                review.getComment(),
                review.getRating()
        );
    }
}
