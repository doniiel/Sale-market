package com.ecom.sale.mapper;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderDto toDto(Order order);
}