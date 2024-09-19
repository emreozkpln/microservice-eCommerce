package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.review.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
        List<String> images = product.getImageUrl()
                .stream()
                .toList();
        List<ReviewResponse> reviews = product.getComments()
                .entrySet()
                .stream()
                .map(entry -> new ReviewResponse(entry.getKey(), entry.getValue()))
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getProduct_name(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                images,
                reviews
        );
    }

    public Product toEntity(ProductRequest request){
        return Product.builder()
                .id(request.id())
                .product_name(request.product_name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .comments(new HashMap<>())
                .build();
    }
}
