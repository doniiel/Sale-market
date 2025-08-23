package com.ecom.sale.controller;

import com.ecom.sale.dto.PaymentDto;
import com.ecom.sale.dto.request.PaymentRequest;
import com.ecom.sale.enums.PaymentMethod;
import com.ecom.sale.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create a new payment for an order", description = "Creates a payment record for the given order with an optional payment method.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successfully created"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping("/{orderId}/create")
    public ResponseEntity<PaymentDto> createPayment(
            @Parameter(description = "ID of the order to create a payment for")
            @PathVariable Long orderId,
            @Parameter(description = "Optional payment method")
            @RequestParam(required = false) PaymentMethod paymentMethod
    ) {
        return ResponseEntity.ok(paymentService.createPayment(orderId, paymentMethod));
    }

    @Operation(summary = "Delete a payment", description = "Deletes an existing payment by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "ID of the payment to delete")
            @PathVariable Long paymentId
    ) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Make a payment for an order", description = "Processes the payment for the given order with the provided payment details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successfully processed"),
            @ApiResponse(responseCode = "400", description = "Invalid payment request"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentDto> pay(
            @Parameter(description = "ID of the order to pay for")
            @PathVariable Long orderId,
            @RequestBody PaymentRequest request
    ) {
        return ResponseEntity.ok(paymentService.pay(orderId, request));
    }

    @Operation(summary = "Get payment by ID", description = "Retrieves a payment by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPayment(
            @Parameter(description = "ID of the payment to retrieve")
            @PathVariable Long paymentId
    ) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @Operation(summary = "Get payment by order ID", description = "Retrieves the payment associated with a specific order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Order or payment not found")
    })
    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentByOrderId(
            @Parameter(description = "ID of the order to retrieve the payment for")
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderId));
    }

}
