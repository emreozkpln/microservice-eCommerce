package dev.buddly.ecommerce.review;

import dev.buddly.ecommerce.exception.ReviewNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;
    private final ReviewMapper mapper;

    public List<ReviewResponse> getAllReview() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public String createReview(ReviewRequest request) {
        return repository.save(mapper.toEntity(request)).getId();
    }

    public ReviewResponse getReviewById(String reviewId) {
        return repository.findById(reviewId)
                .map(mapper::toResponse)
                .orElseThrow(()->new ReviewNotFoundException(
                        format("Cannot find review:: No review found with the provided ID: %s",reviewId)
                ));
    }

    public void updateReview(ReviewRequest request) {
        var review = repository.findById(request.id())
                .orElseThrow(()->new ReviewNotFoundException(
                        format("Cannot find review:: No review found with the provided ID: %s",request.id())
                ));
        mergerReview(review,request);
        repository.save(review);
    }

    private void mergerReview(Review review, ReviewRequest request) {
        if(StringUtils.isNotBlank(request.comment())){
            review.setComment(request.comment());
        }
        if(request.rating()>= 1.0 && request.rating()<=5.0){
            review.setRating(request.rating());
        }
    }

    public void deleteReview(String reviewId) {
        repository.deleteById(reviewId);
    }
}
