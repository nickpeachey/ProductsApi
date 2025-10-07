package com.cawooka.productsapi;

import org.springframework.boot.SpringApplication;

public class TestProductsApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(ProductsApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
