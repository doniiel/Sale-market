package com.ecom.sale.controller;

import com.ecom.sale.dto.PaymentDto;
import com.ecom.sale.dto.request.PaymentRequest;
import com.ecom.sale.enums.PaymentMethod;
import com.ecom.sale.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/create")
    public ResponseEntity<PaymentDto> createPayment(
            @PathVariable Long orderId,
            @RequestParam(required = false)PaymentMethod paymentMethod
    ) {
        return ResponseEntity.ok(paymentService.createPayment(orderId, paymentMethod));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Long paymentId
    ) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentDto> pay(
            @PathVariable Long orderId,
            @RequestBody PaymentRequest request
            ) {
        return ResponseEntity.ok(paymentService.pay(orderId, request));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPayment(
            @PathVariable Long paymentId
    ) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentByOrderId(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderId));
    }

}
