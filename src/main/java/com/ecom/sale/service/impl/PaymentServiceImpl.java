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
import com.ecom.sale.util.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    private static final String API = "/payments";

    @Override
    @Transactional
    public PaymentDto createPayment(Long orderId, PaymentMethod paymentMethod) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Order not found with id=" + orderId));

        if (order.getPayment() != null) {
            log.info("Payment already exists: orderId={}", orderId);
            return mapper.toDto(order.getPayment());
        }

        var payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : CASH);
        payment.setPaymentStatus(PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());

        paymentRepository.save(payment);

        log.info("Created payment: paymentId={}, orderId={}, amount={}, method={}",
                payment.getId(), orderId, payment.getAmount(), payment.getPaymentMethod());
        return mapper.toDto(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        var currentUser = securityUtils.getCurrentUser(API);
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Payment not found with id=" + paymentId));

        var order = payment.getOrder();
        securityUtils.validateAccess(currentUser, order.getUser().getId(), API);

        order.setPayment(null);
        order.setStatus(NEW);
        order.setPaidAt(null);
        orderRepository.save(order);

        paymentRepository.delete(payment);

        log.info("Deleted payment: paymentId={}, reset orderId={}", paymentId, order.getId());
    }

    @Override
    @Transactional
    public PaymentDto pay(Long orderId, PaymentRequest request) {
        var currentUser = securityUtils.getCurrentUser(API);
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Order not found with id=" + orderId));
        securityUtils.hasPermission(currentUser, order.getUser(), API);

        var payment = order.getPayment();
        if (payment == null) {
            throw exception(HttpStatus.NOT_FOUND, "No payment found for orderId=" + orderId);
        }

        ValidatorUtils.validateAmount(payment.getAmount(), request.getAmount());

        payment.setPaymentStatus(APPROVED);
        order.setStatus(PAID);

        orderRepository.save(order);
        paymentRepository.save(payment);

        log.info("Payment approved: paymentId={}, orderId={}, amount={}",
                payment.getId(), orderId, payment.getAmount());
        return mapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Payment not found with id=" + id));
        log.info("Fetched payment: paymentId={}, orderId={}, amount={}",
                payment.getId(), payment.getOrder().getId(), payment.getAmount());
        return mapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByOrder(Long orderId) {
        var payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Payment not found with orderId=" + orderId));
        log.info("Fetched payment by order: paymentId={}, orderId={}, amount={}",
                payment.getId(), orderId, payment.getAmount());
        return mapper.toDto(payment);
    }

    private CustomException exception(HttpStatus status, String message) {
        return new CustomException(API, status, message, LocalDateTime.now());
    }
}
