package com.ecom.sale.service.impl;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.dto.request.OrderRequest;
import com.ecom.sale.enums.Role;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.mapper.OrderMapper;
import com.ecom.sale.model.Order;
import com.ecom.sale.model.OrderItem;
import com.ecom.sale.model.Product;
import com.ecom.sale.repository.OrderRepository;
import com.ecom.sale.repository.PaymentRepository;
import com.ecom.sale.repository.ProductRepository;
import com.ecom.sale.service.OrderService;
import com.ecom.sale.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ecom.sale.enums.OrderStatus.CANCELLED;
import static com.ecom.sale.enums.OrderStatus.NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final SecurityUtils securityUtils;

    private static final String API = "/orders";

    @Override
    @Transactional
    public OrderDto createOrder(OrderRequest request) {
        var currentUser = securityUtils.getCurrentUser(API);
        validateRequest(request);

        var order = new Order();
        order.setStatus(NEW);
        order.setUser(currentUser);

        var items = buildOrderItems(order, request);
        order.setOrderItems(items);
        order.setTotalAmount(calculateTotalAmount(items));

        orderRepository.save(order);
        log.info("Created order: id={}, totalAmount={}", order.getId(), order.getTotalAmount());

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(Long orderId, OrderRequest request) {
        var currentUser = securityUtils.getCurrentUser(API);
        validateRequest(request);

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Order not found with id=" + orderId));
        securityUtils.hasPermission(currentUser, order.getUser(), API);

        restoreProductQuantities(order.getOrderItems());
        order.getOrderItems().clear();

        var newItems = buildOrderItems(order, request);
        order.getOrderItems().addAll(newItems);
        order.setTotalAmount(calculateTotalAmount(newItems));
        order.setStatus(NEW);

        orderRepository.save(order);
        log.info("Updated order: id={}, totalAmount={}", order.getId(), order.getTotalAmount());

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        var currentUser = securityUtils.getCurrentUser(API);
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Order not found with id=" + orderId));

        securityUtils.validateAccess(currentUser, order.getUser().getId(), API);

        if (order.getPayment() != null) {
            paymentRepository.delete(order.getPayment());
            log.info("Deleted payment for order: orderId={}", orderId);
        }

        if (order.getStatus() == NEW) {
            restoreProductQuantities(order.getOrderItems());
            log.info("Restored stock for deleted order id={}", orderId);
        }

        orderRepository.delete(order);
        log.info("Deleted order: id={}", orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Order not found with id=" + id));
        log.info("Fetched order: id={}, totalAmount={}", order.getId(), order.getTotalAmount());
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(Pageable pageable) {
        var currentUser = securityUtils.getCurrentUser(API);

        Page<Order> orders;

        if (currentUser.getRoles().stream().anyMatch(r -> r.getName().equals(Role.ROLE_ADMIN.name()))) {
            orders = orderRepository.findAll(pageable);
        } else {
            orders = orderRepository.findAllByUser_Id(currentUser.getId(), pageable);
        }

        log.info("Fetched orders: total={}", orders.getTotalElements());
        return orders.map(orderMapper::toDto);
    }


    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        var currentUser = securityUtils.getCurrentUser(API);
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Order not found with id=" + orderId));
        securityUtils.hasPermission(currentUser, order.getUser(), API);

        restoreProductQuantities(order.getOrderItems());
        order.setStatus(CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        orderRepository.save(order);

        log.info("Cancelled order: id={}, totalAmount={}", order.getId(), order.getTotalAmount());
    }

    private void validateRequest(OrderRequest request) {
        if (request.getProductIds().size() != request.getQuantities().size()) {
            throw exception(HttpStatus.BAD_REQUEST, "Product IDs and quantities must match in size");
        }
    }

    private List<OrderItem> buildOrderItems(Order order, OrderRequest request) {
        var products = productRepository.findAllByIdIn(request.getProductIds());

        if (products.size() != request.getProductIds().size()) {
            throw exception(HttpStatus.NOT_FOUND, "One or more products not found");
        }

        var items = new ArrayList<OrderItem>();
        for (int i = 0; i < products.size(); i++) {
            var product = products.get(i);
            int quantity = request.getQuantities().get(i);

            validateQuantity(quantity);
            checkStock(product, quantity);

            items.add(buildOrderItem(order, product, quantity));
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            log.info("Reserved product '{}' x{} for order", product.getName(), quantity);
        }
        return items;
    }

    private OrderItem buildOrderItem(Order order, Product product, int quantity) {
        var item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void restoreProductQuantities(List<OrderItem> items) {
        for (var item : items) {
            var product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
            log.info("Restored {} units to product '{}'", item.getQuantity(), product.getName());
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw exception(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
    }

    private void checkStock(Product product, int orderQuantity) {
        if (product.getQuantity() < orderQuantity) {
            throw exception(HttpStatus.BAD_REQUEST, "Not enough stock for product: " + product.getName());
        }
    }

    private CustomException exception(HttpStatus status, String message) {
        return new CustomException(API, status, message, LocalDateTime.now());
    }
}
