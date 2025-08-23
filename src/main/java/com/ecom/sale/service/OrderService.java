package com.ecom.sale.service;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.dto.request.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto createOrder(OrderRequest request);

    OrderDto updateOrder(Long orderId, OrderRequest request);

    void deleteOrder(Long orderId);

    OrderDto getOrder(Long id);

    Page<OrderDto> getOrders(Pageable pageable);

    void cancelOrder(Long orderId);

}
