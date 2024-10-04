package dev.buddly.ecommerce.product;

import dev.buddly.ecommerce.image.ImageClient;
import dev.buddly.ecommerce.image.ImageResponse;
import dev.buddly.ecommerce.review.ReviewClient;
import dev.buddly.ecommerce.review.ReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private ImageClient client;
    @Mock
    private ReviewClient reviewClient;
    @Captor
    ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_return_all_products(){
        //Given
        List<Product> products = new ArrayList<>();
        products.add(
                new Product(
                        1,
                        "test",
                        "test",
                        new BigDecimal("1.00"),
                        1,
                        new ArrayList<>(),
                        new HashMap<>()
                )
        );
        //Mock the calls
        when(productRepository.findAll()).thenReturn(products);
        List<ReviewResponse> reviews = products.get(0).getComments()
                .entrySet()
                .stream()
                .map(entry -> new ReviewResponse(entry.getKey(), entry.getValue()))
                .toList();
        when(mapper.toResponse(any(Product.class))).thenReturn(
                new ProductResponse(
                        1,
                        "test",
                        "test",
                        new BigDecimal("1.00"),
                        1,
                        new ArrayList<>(),
                        reviews
                )
        );
        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(products.size(),response.size());
        verify(productRepository,times(1)).findAll();
    }

    @Test
    void should_return_product_by_id(){
        Integer productId = 1;
        Product product = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                new HashMap<>()
        );

        List<ReviewResponse> reviews = product.getComments()
                .entrySet()
                .stream()
                .map(entry -> new ReviewResponse(entry.getKey(), entry.getValue()))
                .toList();
        ProductResponse response = new ProductResponse(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                reviews
        );
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mapper.toResponse(any(Product.class))).thenReturn(response);

        ProductResponse productResponse = productService.getProductById(productId);
        assertEquals(response.product_name(),productResponse.product_name());

        verify(productRepository,times(1)).findById(productId);
    }

    @Test
    void should_successfully_save_a_product(){
        ProductRequest request = new ProductRequest(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1
        );
        Product savedProduct = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                new HashMap<>()
        );

        when(mapper.toEntity(request)).thenReturn(savedProduct);
        when(productRepository.save(savedProduct)).thenReturn(savedProduct);

        Integer id = productService.createProduct(request);
        assertEquals(request.id(),id);

        verify(mapper,times(1)).toEntity(request);
        verify(productRepository,times(1)).save(savedProduct);
    }

    @Test
    void should_successfully_updated_product(){
        Product existingProduct  = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                new HashMap<>()
        );
        ProductRequest request = new ProductRequest(
                1,
                "test1",
                "test1",
                new BigDecimal("11.00"),
                11
        );

        when(productRepository.findById(request.id())).thenReturn(Optional.of(existingProduct ));

        productService.updateProduct(request);

        verify(productRepository, times(1)).findById(request.id());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void should_successfully_deleted_product(){
        Integer productId = 1;
        Product product = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                new HashMap<>()
        );
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository,times(1)).findById(productId);
        verify(productRepository,times(1)).delete(product);
    }

    @Test
    void should_successfully_add_image_to_product(){
        ImageResponse imageResponse = new ImageResponse(
                1,
                "url"
        );
        Product product = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                new HashMap<>()
        );

        when(client.getImageById(imageResponse.id())).thenReturn(imageResponse);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.addImageToProduct(product.getId(),imageResponse.id());

        verify(client,times(1)).getImageById(imageResponse.id());
        verify(productRepository,times(1)).findById(product.getId());
    }

    @Test
    void should_successfully_add_review_to_product(){
        String reviewId = "12345";
        ReviewResponse reviewResponse = new ReviewResponse(
                "Comment",
                4.5
        );
        Product product = new Product(
                1,
                "test",
                "test",
                new BigDecimal("1.00"),
                1,
                new ArrayList<>(),
                new HashMap<>()
        );

        when(reviewClient.getReviewById(reviewId)).thenReturn(reviewResponse);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.addReviewToProduct(product.getId(),reviewId);

        verify(reviewClient,times(1)).getReviewById(reviewId);
        verify(productRepository,times(1)).findById(product.getId());
    }

}
