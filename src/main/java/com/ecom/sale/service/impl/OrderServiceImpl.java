package com.ecom.sale.service.impl;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.dto.request.OrderRequest;
import com.ecom.sale.mapper.OrderMapper;
import com.ecom.sale.model.Order;
import com.ecom.sale.model.OrderItem;
import com.ecom.sale.model.Product;
import com.ecom.sale.repository.OrderItemRepository;
import com.ecom.sale.repository.OrderRepository;
import com.ecom.sale.repository.ProductRepository;
import com.ecom.sale.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ecom.sale.enums.OrderStatus.CANCELLED;
import static com.ecom.sale.enums.OrderStatus.NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMappper;

    @Override
    @Transactional
    public OrderDto createOrder(OrderRequest request) {
        validateRequest(request);

        var order = new Order();
        order.setStatus(NEW);

        var items = buildOrderItems(order, request);
        order.setOrderItems(items);
        order.setTotalAmount(calculateTotalAmount(items));

        orderRepository.save(order);

        log.info("Created new order with id: {}", order.getId());
        return orderMappper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(Long id, OrderRequest request) {
        validateRequest(request);

        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        restoreProductQuantities(order.getOrderItems());
        orderItemRepository.deleteAll(order.getOrderItems());


        var updatedItems = buildOrderItems(order, request);
        order.setOrderItems(updatedItems);
        order.setTotalAmount(calculateTotalAmount(updatedItems));
        order.setStatus(NEW);

        orderRepository.save(order);

        log.info("Updated order with id: {}", order.getId());
        return orderMappper.toDto(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() == NEW) {
            restoreProductQuantities(order.getOrderItems());
        }

        orderItemRepository.deleteAll(order.getOrderItems());
        orderRepository.delete(order);

        log.info("Deleted order with id: {}", orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id)
                .map(orderMappper::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMappper::toDto);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        restoreProductQuantities(order.getOrderItems());

        order.setStatus(CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled: {}", order);
    }


    private void validateRequest(OrderRequest request) {
        if (request.getProductIds().size() != request.getQuantities().size()) {
            throw new IllegalArgumentException("Product IDs and quantities must match in size");
        }
    }

    private List<OrderItem> buildOrderItems(Order order, OrderRequest request) {
        var products = productRepository.findAllByIdIn(request.getProductIds());
        var items = new ArrayList<OrderItem>();

        for (int i = 0; i < products.size(); i++) {
            var product = products.get(i);
            var quantity = request.getQuantities().get(i);

            validateQuantity(quantity);
            checkStock(product, quantity);

            items.add(buildOrderItems(order, product, quantity));
        }

        return items;
    }

    private OrderItem buildOrderItems(Order order, Product product, Integer quantity) {
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
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    private void checkStock(Product product, int orderQuantity) {
        if (product.getQuantity() < orderQuantity) {
            throw new IllegalArgumentException(
                    "Not enough stock for product: " + product.getName()
            );
        }
    }
}

