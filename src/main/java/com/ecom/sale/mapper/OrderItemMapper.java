package com.ecom.sale.mapper;

import com.ecom.sale.dto.OrderItemDto;
import com.ecom.sale.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    OrderItemDto toDto(OrderItem orderItem);
}
