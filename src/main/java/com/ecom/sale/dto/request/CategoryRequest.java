package com.ecom.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Request object for creating or updating a category")
public class CategoryRequest {

    @NotBlank(message = "Category name must not be empty")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Schema(description = "Category name (must be unique and non-empty)", example = "Electronics")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Schema(description = "Optional description of the category", example = "Category for electronic gadgets and accessories")
    private String description;
}
