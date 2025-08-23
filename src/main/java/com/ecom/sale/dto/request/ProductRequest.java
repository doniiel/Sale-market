package com.ecom.sale.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductRequest {

    @Schema(description = "Unique identifier of the product category", example = "5")
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @Schema(description = "Name of the product", example = "Wireless Headphones")
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Detailed description of the product", example = "Bluetooth wireless headphones with noise cancellation")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Schema(description = "Price of the product", example = "149.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(description = "Available stock quantity", example = "50")
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}

