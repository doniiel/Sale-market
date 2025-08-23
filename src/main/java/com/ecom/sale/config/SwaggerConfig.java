package com.ecom.sale.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sale Market API")
                        .version("3.0.3")
                        .description("Documentation REST API for sale market")
                        .contact(new Contact()
                                .name("Orynbek Daniyal")
                                .email("orynbekdanial8@gmail.com")
                        )
                )
                .servers(List.of(new Server().url("http://localhost:8080")));
    }

}
