package com.cawooka.productsapi;

import com.cawooka.productsapi.controllers.HomeController;
import com.cawooka.productsapi.entities.Product;
import com.cawooka.productsapi.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(HomeController.class)

class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("GET / returns welcome message")
    void home_returnsWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Products API!"));
    }

    @Test
    @DisplayName("GET /products returns list of products")
    void listProducts_returnsList() throws Exception {
        Product p3 = new Product();
        p3.setId(1L);
        p3.setName("Banana");
        p3.setPrice(BigDecimal.valueOf(1.90));
        Product p4 = new Product();
        p4.setId(2L);
        p4.setName("Apple");
        p4.setPrice(BigDecimal.valueOf(1.99));
        given(productService.findAll()).willReturn(List.of(p3,p4));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Banana")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Apple")));
    }

}
