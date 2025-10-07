package com.cawooka.productsapi.integrations;

import com.cawooka.productsapi.entities.Product;
import com.cawooka.productsapi.repositories.ProductRepository;
import com.cawooka.productsapi.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Tag("integration")
public class ProductServiceIntegrationTests {

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
    }

    @Test
    void testProductCreationAndFetch() {
        // Create
        productService.deleteAll();
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(1.99));
        Product created = productService.create(product);

        // Fetch
        Product fetched = productService.getById(created.getId());

        // Assert
        assertThat(fetched).isNotNull();
        assertThat(fetched.getName()).isEqualTo("Test Product");
        assertThat(fetched.getId()).isEqualTo(created.getId());
    }

    @Test
    void testProductThatDoesntExist() {
        Product fetched = null;
        try {
            fetched = productService.getById(2L);
        } catch (Exception e) {
            // Expected exception
        }
        assertThat(fetched).isNull();
    }

    @Test
    void testFetchAllProducts() {
        createMultipleProducts();
        var products = productService.findAll();
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should find product by name")
    void testFindByName() {
        productRepository.save(Product.builder().name("Gadget").price(new BigDecimal("19.95")).build());
        productRepository.save(Product.builder().name("Thingamajig").price(new BigDecimal("3.50")).build());

        Optional<Product> found = productRepository.findByName("Gadget");
        assertThat(found).isPresent();
        assertThat(found.get().getPrice()).isEqualByComparingTo("19.95");
    }

    @Test
    @DisplayName("should retrieve all products")
    void testFindAll() {
        productRepository.saveAll(List.of(
                Product.builder().name("A").price(new BigDecimal("1.00")).build(),
                Product.builder().name("B").price(new BigDecimal("2.00")).build(),
                Product.builder().name("C").price(new BigDecimal("3.00")).build()
        ));

        List<Product> all = productRepository.findAll();
        assertThat(all).hasSize(3);
        assertThat(all).extracting(Product::getName).containsExactlyInAnyOrder("A", "B", "C");
    }

    void createMultipleProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(10.00));
        productService.create(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(20.00));
        productService.create(product2);
    }
}
