package com.cawooka.productsapi.services;

import com.cawooka.productsapi.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product getById(Long id);
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    void deleteAll();
}
