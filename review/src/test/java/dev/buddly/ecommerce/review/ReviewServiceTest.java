package dev.buddly.ecommerce.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @InjectMocks
    private ReviewService service;
    @Mock
    private ReviewRepository repository;
    @Mock
    private ReviewMapper mapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_successfully_return_reviews(){
        List<Review> reviews = new ArrayList<>();
        reviews.add(
                new Review(
                        "12345",
                        "Comment",
                        4.0
                )
        );

        when(repository.findAll()).thenReturn(reviews);
        when(mapper.toResponse(any(Review.class)))
                .thenReturn(
                new ReviewResponse(
                        "12345",
                        "Comment",
                        4.0
                )
        );

        List<ReviewResponse> reviewResponses = service.getAllReview();

        assertEquals(reviews.size(),reviewResponses.size());
        verify(repository,times(1)).findAll();
    }

    @Test
    void should_successfully_return_review_by_id(){
        String id = "12345";
        Review review = new Review(
                "12345",
                "Comment",
                4.0
        );
        ReviewResponse reviewResponse = new ReviewResponse(
                "12345",
                "Comment",
                4.0
        );

        when(repository.findById(id)).thenReturn(Optional.of(review));
        when(mapper.toResponse(any(Review.class)))
                .thenReturn(reviewResponse);

        ReviewResponse response = service.getReviewById(id);

        assertEquals(reviewResponse.comment(),response.comment());
        verify(repository,times(1)).findById(id);
    }

    @Test
    void should_successfully_create_review(){
        ReviewRequest reviewRequest = new ReviewRequest(
                "12345",
                "Comment",
                4.0
        );
        Review review = new Review(
                "12345",
                "Comment",
                4.0
        );

        when(mapper.toEntity(reviewRequest)).thenReturn(review);
        when(repository.save(review)).thenReturn(review);

        String id = service.createReview(reviewRequest);
        assertEquals(reviewRequest.id(),id);
        verify(mapper,times(1)).toEntity(reviewRequest);
        verify(repository,times(1)).save(review);
    }

    @Test
    void should_successfully_update_review(){
        Review review = new Review(
                "12345",
                "Comment",
                4.0
        );
        ReviewRequest updatedRequest = new ReviewRequest(
                "12345",
                "Comment1",
                4.1
        );

        when(repository.findById(updatedRequest.id())).thenReturn(Optional.of(review));

        service.updateReview(updatedRequest);

        verify(repository,times(1)).findById(updatedRequest.id());
        verify(repository,times(1)).save(review);
    }

    @Test
    void should_successfully_delete_review(){
        String id = "12345";
        doNothing().when(repository).deleteById(id);
        service.deleteReview(id);
        verify(repository,times(1)).deleteById(id);

    }
}
