package com.ecom.sale.dto.request;

import com.ecom.sale.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PaymentRequest {

    private Long orderId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

}
