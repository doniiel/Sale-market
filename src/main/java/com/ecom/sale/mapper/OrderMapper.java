package com.ecom.sale.mapper;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.model.Order;

public interface OrderMapper {

    OrderDto toDto(Order order);

}
