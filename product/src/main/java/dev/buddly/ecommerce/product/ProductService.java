package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.exception.ProductNotFoundException;
import dev.buddly.ecommerce.image.ImageClient;
import dev.buddly.ecommerce.image.ImageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "products",key = "#root.methodName",unless = "#result==null")// if the incoming data value is null,do not cache it.
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> {
                    List<String> images = product.getImageUrl()
                            .stream()
                            .toList();

                    return new ProductResponse(
                            product.getId(),
                            product.getProductName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getStock(),
                            images
                    );
                })
                .collect(Collectors.toList());
    }


    @CacheEvict(value = {"products","product_id"},allEntries = true) //when this method runs, clear the products and product_id caches
    public Integer createProduct(ProductRequest request) {
        return productRepository.save(mapper.toEntity(request)).getId();
    }

    @Cacheable(value = "product_id",key = "#root.methodName + #id",unless = "#result==null")
    public ProductResponse getProductById(Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException(
                        format("Cannot find product:: No product found with the provided ID: %s",id)
                ));
        List<String> images = product.getImageUrl()
                .stream()
                .toList();
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                images
        );
    }

    @CachePut(cacheNames = "product_id",key = "'getProductById'+#request.id()",unless = "#result==null")
    public void updateProduct(ProductRequest request) {
        var product = productRepository.findById(request.id())
                .orElseThrow(() -> new ProductNotFoundException(
                        format("Cannot update product:: No product found with the provided ID: %s",request.id())
                ));
        mergerProduct(product,request);
        productRepository.save(product);
    }

    @CacheEvict(value = {"products","product_id"},allEntries = true)
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

    @CacheEvict(value = {"products","product_id"},allEntries = true)
    public void deleteProduct(Integer productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        format("Cannot delete product:: No product found with the provided ID: %s",productId)
                ));
        productRepository.delete(product);
    }
}
