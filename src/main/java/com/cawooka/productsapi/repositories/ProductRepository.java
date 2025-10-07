package com.cawooka.productsapi.repositories;

import com.cawooka.productsapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);

    // delete all products
    void deleteAll();
}
