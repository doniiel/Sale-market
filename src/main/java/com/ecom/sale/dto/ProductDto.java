package com.ecom.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Schema(description = "Product DTO containing product details")
public class ProductDto {

    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;

    @Schema(description = "Name of the product", example = "Nike Air Max 270")
    private String name;

    @Schema(description = "Category name of the product", example = "Shoes")
    private String categoryName;

    @Schema(description = "Detailed description of the product", example = "Lightweight running shoes with breathable mesh")
    private String description;

    @Schema(description = "Price of the product", example = "149.99")
    private BigDecimal price;

    @Schema(description = "Available stock quantity of the product", example = "50")
    private Integer quantity;
}

