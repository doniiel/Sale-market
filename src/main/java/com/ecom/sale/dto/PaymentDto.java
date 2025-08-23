package com.ecom.sale.dto;

import com.ecom.sale.enums.PaymentMethod;
import com.ecom.sale.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Schema(description = "DTO representing payment details")
public class PaymentDto {

    @Schema(description = "Unique identifier of the payment", example = "1001")
    private Long id;

    @Schema(description = "Associated order ID", example = "2001")
    private Long orderId;

    @Schema(description = "Payment method used", example = "CASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Current status of the payment", example = "APPROVED")
    private PaymentStatus paymentStatus;

    @Schema(description = "Amount paid", example = "249.99")
    private BigDecimal amount;

    @Schema(description = "Transaction identifier from the payment provider", example = "txn_abc123xyz")
    private String transactionId;
}
