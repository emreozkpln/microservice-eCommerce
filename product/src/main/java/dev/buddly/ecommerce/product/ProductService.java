package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.exception.ProductNotFoundException;
import dev.buddly.ecommerce.image.ImageClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final ImageClient client;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public Integer createProduct(ProductRequest request) {
        return productRepository.save(mapper.toEntity(request)).getId();
    }

    public void updateProduct(ProductRequest request) {
        var product = productRepository.findById(request.id())
                .orElseThrow(() -> new ProductNotFoundException(
                        format("Cannot update product:: No product found with the provided ID: %s",request.id())
                ));
        mergerProduct(product,request);
        productRepository.save(product);
    }

    public void addImageToProduct(Integer productId,Integer imageId){
        String image = client.getImageById(imageId).getBody().imageUrl();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        format("Cannot update product:: No product found with the provided ID: %s",productId)
                ));
        product.getImageUrl()
                .add(image);
        productRepository.save(product);
    }

    private void mergerProduct(Product product, ProductRequest request) {
        if(StringUtils.isNotBlank(request.productName())){
            product.setProductName(request.productName());
        }
        if(StringUtils.isNotBlank(request.description())){
            product.setDescription(request.description());
        }
        if(request.price() != null && request.price().compareTo(BigDecimal.ZERO) > 0){
            product.setPrice(request.price());
        }
        if(request.stock() >= 0){
            product.setStock(request.stock());
        }
    }

    public void deleteProduct(Integer productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        format("Cannot delete product:: No product found with the provided ID: %s",productId)
                ));
        productRepository.delete(product);
    }
}
