package com.ecom.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Schema(description = "Data Transfer Object representing an item in an order")
public class OrderItemDto {

    @Schema(description = "Unique identifier of the order item", example = "101")
    private Long id;

    @Schema(description = "Identifier of the product associated with this item", example = "501")
    private Long productId;

    @Schema(description = "Quantity of the product in the order", example = "3")
    private Integer quantity;

    @Schema(description = "Unit price of the product at the time of purchase", example = "49.99")
    private BigDecimal unitPrice;

    @Schema(description = "Total price for this order item (unitPrice × quantity)", example = "149.97")
    private BigDecimal totalPrice;
}
