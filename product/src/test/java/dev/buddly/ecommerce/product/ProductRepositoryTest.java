package dev.buddly.ecommerce.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    ProductRepository productRepository;

    @Test
    void connectionEstablished(){
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp(){
        Product product = new Product(
                1,
                "Test Product",
                "Test Description",
                new BigDecimal("100.00"),
                100,
                new ArrayList<>(),
                new HashMap<>()
        );
        productRepository.save(product);
    }

    @AfterEach
    void tearDown(){
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnProductByProductName() {
        Product product = productRepository.findByDescription("Test Description");
        assertThat(product).isNotNull();
    }
}
