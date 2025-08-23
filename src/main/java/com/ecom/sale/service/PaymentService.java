package com.ecom.sale.service;

import com.ecom.sale.dto.PaymentDto;
import com.ecom.sale.dto.request.PaymentRequest;
import com.ecom.sale.enums.PaymentMethod;

public interface PaymentService {

    PaymentDto createPayment(Long orderId, PaymentMethod paymentMethod);

    void deletePayment(Long paymentId);

    PaymentDto pay(Long orderId, PaymentRequest request);

    PaymentDto getPaymentById(Long id);

    PaymentDto getPaymentByOrder(Long orderId);
}
