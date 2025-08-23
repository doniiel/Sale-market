package com.ecom.sale.dto;

import com.ecom.sale.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Schema(description = "DTO representing an order with its items")
public class OrderDto {

    @Schema(description = "Unique identifier of the order", example = "101")
    private Long id;

    @Schema(description = "Current status of the order", example = "NEW")
    private OrderStatus status;

    @Schema(description = "Total amount of the order", example = "2500.75")
    private BigDecimal totalAmount;

    @Schema(description = "Time when the order was paid", example = "2025-08-23T12:30:45")
    private LocalDateTime paidAt;

    @Schema(description = "Time when the order was cancelled", example = "2025-08-24T15:10:20")
    private LocalDateTime cancelledAt;

    @Schema(description = "List of items in the order")
    private List<OrderItemDto> orderItems;
}
