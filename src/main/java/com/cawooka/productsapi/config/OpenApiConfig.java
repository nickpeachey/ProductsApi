package com.cawooka.productsapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Products API")
                        .description("REST API for managing products")
                        .version("v1")
                        .contact(new Contact().name("Cawooka").url("https://example.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}


