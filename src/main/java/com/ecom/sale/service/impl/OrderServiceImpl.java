package com.ecom.sale.service.impl;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.dto.request.OrderRequest;
import com.ecom.sale.mapper.OrderMapper;
import com.ecom.sale.model.Order;
import com.ecom.sale.model.OrderItem;
import com.ecom.sale.repository.OrderItemRepository;
import com.ecom.sale.repository.OrderRepository;
import com.ecom.sale.repository.ProductRepository;
import com.ecom.sale.service.OrderService;
import com.ecom.sale.util.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ecom.sale.enums.OrderStatus.NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMappper;
    private final UpdateUtils updateUtils;

    @Override
    @Transactional
    public OrderDto createOrder(OrderRequest request) {
        validateOrderMatch(request.getProductIds(), request.getQuantities());

        var order = new Order();
        order.setStatus(NEW);

        var products = productRepository.findAllByIdIn(request.getProductIds());
        var orderItems = new ArrayList<OrderItem>();
        var totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < products.size(); i++) {
            var product = products.get(i);
            var quantity = request.getQuantities().get(i);

            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            totalAmount = totalAmount.add(orderItem.getTotalPrice());
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        orderRepository.save(order);

        log.info("Order created: {}", order);
        return orderMappper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(Long id, OrderRequest request) {
        validateOrderMatch(request.getProductIds(), request.getQuantities());

        var order =

    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        orderItemRepository.deleteAll(order.getOrderItems());
        orderRepository.delete(order);

        log.info("Order deleted: {}", order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return orderMappper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMappper::toDto);
    }

    private void validateOrderMatch(List<Long> productIds, List<Integer> quantities) {
        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Product IDs and quantities must match in size");
        }
    }
}
