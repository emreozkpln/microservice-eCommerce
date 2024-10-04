package dev.buddly.ecommerce.product;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    private final String API_PATH = "/v1/product";

    @Test
    void shouldCreateProduct(){
        //given
        ProductRequest request = new ProductRequest(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1
        );
        //when
        ResponseEntity<Integer> createProductResponse = restTemplate.exchange(
                API_PATH,
                POST,
                new HttpEntity<>(request),
                Integer.class
        );
        //then
        assertThat(createProductResponse.getStatusCode()).isEqualTo(OK);

        //get all products request
        ResponseEntity<List<ProductResponse>> allProductsResponse = restTemplate.exchange(
                API_PATH,
                GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {}
        );
        assertThat(allProductsResponse.getStatusCode()).isEqualTo(OK);

        ProductResponse productCreated = Objects.requireNonNull(allProductsResponse.getBody())
                .stream()
                .filter(c->c.product_name().equals(request.product_name()))
                .findFirst()
                .orElseThrow();
        //comparison of product we created with create product request
        assertThat(productCreated.id()).isEqualTo(request.id());
        assertThat(productCreated.product_name()).isEqualTo(request.product_name());
        assertThat(productCreated.description()).isEqualTo(request.description());
        assertThat(productCreated.price()).isEqualTo(request.price());
        assertThat(productCreated.stock()).isEqualTo(request.stock());

    }

    @Test
    void shouldUpdateProduct(){
        //given
        ProductRequest request = new ProductRequest(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1
        );

        //when
        ResponseEntity<Integer> createProductResponse = restTemplate.exchange(
                API_PATH,
                POST,
                new HttpEntity<>(request),
                Integer.class
        );
        assertThat(createProductResponse.getStatusCode()).isEqualTo(OK);

        ProductRequest updatedRequest = new ProductRequest(
                1,
                "test1",
                "test1",
                new BigDecimal("11.00"),
                11
        );
        ResponseEntity<Void> updateProductResponse = restTemplate.exchange(
                API_PATH,
                PUT,
                new HttpEntity<>(updatedRequest),
                Void.class
        );
        assertThat(updateProductResponse.getStatusCode()).isEqualTo(ACCEPTED);

        ResponseEntity<List<ProductResponse>> allProductsResponse = restTemplate.exchange(
                API_PATH,
                GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {}
        );
        assertThat(allProductsResponse.getStatusCode()).isEqualTo(OK);

        ProductResponse productUpdated = Objects.requireNonNull(allProductsResponse.getBody())
                .stream()
                .filter(c->c.product_name().equals(updatedRequest.product_name()))
                .findFirst()
                .orElseThrow();
        //comparison of product we created with create product request
        assertThat(productUpdated.id()).isEqualTo(updatedRequest.id());
        assertThat(productUpdated.product_name()).isEqualTo(updatedRequest.product_name());
        assertThat(productUpdated.description()).isEqualTo(updatedRequest.description());
        assertThat(productUpdated.price()).isEqualTo(updatedRequest.price());
        assertThat(productUpdated.stock()).isEqualTo(updatedRequest.stock());
    }

    @Test
    void shouldDeleteProduct(){
        ProductRequest request = new ProductRequest(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1
        );

        ResponseEntity<Integer> createProductResponse = restTemplate.exchange(
                API_PATH,
                POST,
                new HttpEntity<>(request),
                Integer.class
        );
        assertThat(createProductResponse.getStatusCode()).isEqualTo(OK);

        ResponseEntity<List<ProductResponse>> allProductResponse = restTemplate.exchange(
                API_PATH,
                GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {}
        );
        assertThat(allProductResponse.getStatusCode()).isEqualTo(OK);

        Integer id = Objects.requireNonNull(allProductResponse.getBody())
                .stream()
                .map(ProductResponse::id)
                .findFirst()
                .orElseThrow();

        restTemplate.exchange(
                API_PATH + "/" + id,
                DELETE,
                null,
                Void.class
        ).getStatusCode().is2xxSuccessful();

        //get customer by id after we deleted that product
        ResponseEntity<String> productResponseById = restTemplate.exchange(
                API_PATH + "/id/" + id,
                GET,
                null,
                String.class
        );
        assertThat(productResponseById.getStatusCode()).isEqualTo(NOT_FOUND);
    }

}
