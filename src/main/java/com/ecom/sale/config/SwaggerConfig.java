package com.ecom.sale.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Sale Market API",
                version = "1.0",
                description = "Documentation REST API for sale market",
                contact = @Contact(
                        name = "Orynbek Daniyal",
                        email = "orynbekdanial8@gmail.com"
                )
        )
)
public class SwaggerConfig {
}
