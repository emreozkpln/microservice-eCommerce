package dev.buddly.ecommerce.product;

import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl()
        );
    }

    public Product toEntity(ProductRequest request){
        return Product.builder()
                .id(request.id())
                .productName(request.productName())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .build();
    }
}
