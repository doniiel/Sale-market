package com.ecom.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequest {

    @Schema(description = "List of product IDs included in the order", example = "[101, 102, 103]", required = true)
    @NotEmpty(message = "Product IDs list must not be empty")
    private List<Long> productIds;

    @Schema(description = "List of quantities corresponding to each product", example = "[2, 1, 5]", required = true)
    @NotEmpty(message = "Quantities list must not be empty")
    private List<Integer> quantities;

}
