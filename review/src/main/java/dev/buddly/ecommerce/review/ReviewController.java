package dev.buddly.ecommerce.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/review")
public class ReviewController {

    private final ReviewService service;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReview(){
        return ResponseEntity.ok(service.getAllReview());
    }

    @GetMapping("/id/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @PathVariable String reviewId
    ){
        return ResponseEntity.ok(service.getReviewById(reviewId));
    }

    @PostMapping
    public ResponseEntity<String> createReview(
            @RequestBody @Valid ReviewRequest request
    ){
        return ResponseEntity.ok(service.createReview(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateReview(
            @RequestBody @Valid ReviewRequest request
    ){
        service.updateReview(request);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String reviewId
    ){
        service.deleteReview(reviewId);
        return ResponseEntity.accepted().build();
    }
}
