package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.review.ReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp(){
        mapper = new ProductMapper();
    }

    @Test
    void shouldMapProductToEntity(){
        ProductRequest request = new ProductRequest(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1
        );

        Product product = mapper.toEntity(request);

        assertEquals(request.id(),product.getId());
        assertEquals(request.product_name(),product.getProduct_name());
        assertEquals(request.description(),product.getDescription());
        assertEquals(request.price(),product.getPrice());
        assertEquals(request.stock(),product.getStock());
    }

    @Test
    void shouldMapProductToResponse(){
        Product request = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                Map.of("Great product", 4.5, "Could be better", 3.0)
        );

        ProductResponse response = mapper.toResponse(request);

        List<ReviewResponse> reviews = request.getComments()
                .entrySet()
                .stream()
                .map(entry -> new ReviewResponse(entry.getKey(), entry.getValue()))
                .toList();


        assertEquals(request.getId(),response.id());
        assertEquals(request.getProduct_name(),response.product_name());
        assertEquals(request.getDescription(),response.description());
        assertEquals(request.getPrice(),response.price());
        assertEquals(request.getStock(),response.stock());
        assertEquals(request.getImageUrl(),response.imageUrl());
        assertEquals(reviews,response.comments());
    }


}
