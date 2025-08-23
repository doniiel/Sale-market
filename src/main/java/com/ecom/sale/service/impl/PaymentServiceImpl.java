package com.ecom.sale.service.impl;

import com.ecom.sale.dto.PaymentDto;
import com.ecom.sale.dto.request.PaymentRequest;
import com.ecom.sale.enums.PaymentMethod;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.mapper.PaymentMapper;
import com.ecom.sale.model.Payment;
import com.ecom.sale.repository.OrderRepository;
import com.ecom.sale.repository.PaymentRepository;
import com.ecom.sale.service.PaymentService;
import com.ecom.sale.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.ecom.sale.enums.OrderStatus.NEW;
import static com.ecom.sale.enums.OrderStatus.PAID;
import static com.ecom.sale.enums.PaymentMethod.CASH;
import static com.ecom.sale.enums.PaymentStatus.APPROVED;
import static com.ecom.sale.enums.PaymentStatus.PENDING;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper mapper;

    @Override
    @Transactional
    public PaymentDto createPayment(Long orderId, PaymentMethod paymentMethod) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(
                        "/payments", HttpStatus.NOT_FOUND,
                        "Order not found with id: " + orderId,
                        LocalDateTime.now()
                ));

        if (order.getPayment() != null) {
            log.warn("Payment already exists for orderId={}", orderId);
            return mapper.toDto(order.getPayment());
        }

        var payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : CASH);
        payment.setPaymentStatus(PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());

        paymentRepository.save(payment);

        log.info("Created payment for orderId={}", orderId);
        return mapper.toDto(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(
                        "/payments", HttpStatus.NOT_FOUND,
                        "Payment not found with id: " + paymentId,
                        LocalDateTime.now()
                ));

        var order = payment.getOrder();
        order.setStatus(NEW);
        order.setPaidAt(null);
        orderRepository.save(order);

        paymentRepository.deleteById(paymentId);

        log.info("Deleted payment with id={} and reset orderId={}", paymentId, order.getId());
    }

    @Override
    @Transactional
    public PaymentDto pay(Long orderId, PaymentRequest request) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(
                        "/payments", HttpStatus.NOT_FOUND,
                        "Order not found with id: " + orderId,
                        LocalDateTime.now()
                ));

        var payment = order.getPayment();
        if (payment == null) {
            throw new CustomException(
                    "/payments", HttpStatus.NOT_FOUND,
                    "No payment found for orderId=" + orderId,
                    LocalDateTime.now()
            );
        }

        ValidatorUtils.validateAmount(payment.getAmount(), request.getAmount());

        payment.setPaymentStatus(APPROVED);
        order.setStatus(PAID);
        orderRepository.save(order);
        paymentRepository.save(payment);

        log.info("Approved payment for orderId={}", orderId);
        return mapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new CustomException(
                        "/payments", HttpStatus.NOT_FOUND,
                        "Payment not found with id: " + id,
                        LocalDateTime.now()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByOrder(Long orderId) {
        return paymentRepository.findByOrder_Id(orderId)
                .map(mapper::toDto)
                .orElseThrow(() -> new CustomException(
                        "/payments", HttpStatus.NOT_FOUND,
                        "Payment not found with order id: " + orderId,
                        LocalDateTime.now()
                ));
    }
}
