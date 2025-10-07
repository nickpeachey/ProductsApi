package com.cawooka.productsapi.controllers;

import com.cawooka.productsapi.entities.Product;
import com.cawooka.productsapi.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {
    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String home() {
        return "Welcome to the Products API!";
    }

    // List all products
    @GetMapping("products")
    public List<Product> listProducts(@Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) Sort sort) {
        return productService.findAll();
    }

    // Get a single product by id
    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getById(id);
    }

    // Create a new product
    @PostMapping("products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.create(product);
        return ResponseEntity.created(URI.create("/products/" + created.getId())).body(created);
    }

    // Update an existing product
    @PutMapping("products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    // Delete a product
    @DeleteMapping("products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }

    // Translate not-found to HTTP 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
