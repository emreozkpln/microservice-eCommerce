package dev.buddly.ecommerce.review;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ReviewRepository repository;

    @Autowired
    ReviewMapper mapper;

    private final String API_URL = "/v1/review";

    @Test
    void should_return_all_review(){
        ResponseEntity<List<ReviewResponse>> response = testRestTemplate.exchange(
                API_URL,
                GET,
                null,
                new ParameterizedTypeReference<List<ReviewResponse>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void should_successfully_create_a_review(){
        ReviewRequest request = new ReviewRequest(
                "12345",
                "Comment",
                4.0
        );
        ResponseEntity<String> createReview = testRestTemplate.exchange(
                API_URL,
                POST,
                new HttpEntity<>(request),
                String.class
        );
        assertThat(createReview.getStatusCode()).isEqualTo(OK);

        ResponseEntity<List<ReviewResponse>> allReviewResponse = testRestTemplate.exchange(
                API_URL,
                GET,
                null,
                new ParameterizedTypeReference<List<ReviewResponse>>() {}
        );
        assertThat(allReviewResponse.getStatusCode()).isEqualTo(OK);

        ReviewResponse reviewCreated = Objects.requireNonNull(allReviewResponse.getBody())
                .stream()
                .filter(c->c.comment().equals(request.comment()))
                .findFirst()
                .orElseThrow();

        assertThat(request.id()).isEqualTo(reviewCreated.id());
        assertThat(request.comment()).isEqualTo(reviewCreated.comment());
        assertThat(request.rating()).isEqualTo(reviewCreated.rating());
    }

    @Test
    void should_update_review(){
        ReviewRequest createRequest = new ReviewRequest(
                "12345",
                "Comment",
                4.0
        );
        ResponseEntity<String> createReviewResponse = testRestTemplate.exchange(
                API_URL,
                POST,
                new HttpEntity<>(createRequest),
                String.class
        );
        assertThat(createReviewResponse.getStatusCode()).isEqualTo(OK);

        ReviewRequest updateRequest = new ReviewRequest(
                "12345",
                "Comment1",
                4.1
        );
        ResponseEntity<Void> updateReviewResponse = testRestTemplate.exchange(
                API_URL,
                PUT,
                new HttpEntity<>(createRequest),
                Void.class
        );
        assertThat(updateReviewResponse.getStatusCode()).isEqualTo(ACCEPTED);

        ResponseEntity<List<ReviewResponse>> allReviewResponse = testRestTemplate.exchange(
                API_URL,
                GET,
                null,
                new ParameterizedTypeReference<List<ReviewResponse>>() {}
        );
        assertThat(allReviewResponse.getStatusCode()).isEqualTo(OK);

        ReviewResponse reviewUpdated = Objects.requireNonNull(allReviewResponse.getBody())
                .stream()
                .filter(c->c.comment().equals(updateRequest.comment()))
                .findFirst()
                .orElseThrow();

        assertThat(reviewUpdated.id()).isEqualTo(updateRequest.id());
        assertThat(reviewUpdated.comment()).isEqualTo(updateRequest.comment());
        assertThat(reviewUpdated.rating()).isEqualTo(updateRequest.rating());

    }

    @Test
    void should_delete_review(){
        ReviewRequest createRequest = new ReviewRequest(
                "12345",
                "Comment",
                4.0
        );
        ResponseEntity<String> createReviewResponse = testRestTemplate.exchange(
                API_URL,
                POST,
                new HttpEntity<>(createRequest),
                String.class
        );
        assertThat(createReviewResponse.getStatusCode()).isEqualTo(OK);

        ResponseEntity<List<ReviewResponse>> allReviewResponse = testRestTemplate.exchange(
                API_URL,
                GET,
                null,
                new ParameterizedTypeReference<List<ReviewResponse>>() {}
        );

        assertThat(allReviewResponse.getStatusCode()).isEqualTo(OK);

        String reviewId = Objects.requireNonNull(allReviewResponse.getBody())
                .stream()
                .map(ReviewResponse::id)
                .findFirst()
                .orElseThrow();

        testRestTemplate.exchange(
                API_URL + "/" + reviewId,
                DELETE,
                null,
                Void.class
        ).getStatusCode().is2xxSuccessful();

        ResponseEntity<String> reviewById = testRestTemplate.exchange(
                API_URL + "/id/" + reviewId,
                GET,
                null,
                String.class
        );
        assertThat(reviewById.getStatusCode()).isEqualTo(NOT_FOUND);

    }
}
