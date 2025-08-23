package com.ecom.sale.dto.request;

import com.ecom.sale.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PaymentRequest {

    @Schema(description = "Unique identifier of the order", example = "101")
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @Schema(description = "Payment amount (must be positive)", example = "150.75")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Schema(description = "Payment method (e.g., CASH, CARD)", example = "CASH")
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
