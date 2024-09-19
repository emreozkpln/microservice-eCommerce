package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.elasticsearch.ProductDocument;
import dev.buddly.ecommerce.elasticsearch.SearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/index/{index}")
    public ResponseEntity<List<ProductDocument>> getAllIndex(
            @PathVariable String index
    ){
        return ResponseEntity.ok(productService.getAllDataFromIndex(index));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProductsByFieldAndValue(
            @RequestBody SearchRequest request
    ){
        return ResponseEntity.ok(productService.searchProductsByFieldAndValue(request));
    }

    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ProductRequest request
    ){
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/image/{productId}/{imageId}")
    public ResponseEntity<Void> addImageToProduct(
            @PathVariable Integer productId, @PathVariable Integer imageId){
        productService.addImageToProduct(productId,imageId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/review/{productId}/{reviewId}")
    public ResponseEntity<Void> addImageToProduct(
            @PathVariable Integer productId, @PathVariable String reviewId){
        productService.addReviewToProduct(productId,reviewId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateProduct(
            @RequestBody @Valid ProductRequest request
    ){
        productService.updateProduct(request);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("productId") Integer productId
    ){
        productService.deleteProduct(productId);
        return ResponseEntity.accepted().build();
    }
}
