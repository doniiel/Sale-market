package com.ecom.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Data Transfer Object representing a Category")
public class CategoryDto {

    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;

    @Schema(description = "Name of the category", example = "Electronics")
    private String name;

    @Schema(description = "Description of the category", example = "Category for electronic gadgets and accessories")
    private String description;
}
