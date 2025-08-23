package com.ecom.sale.mapper;

import com.ecom.sale.dto.PaymentDto;
import com.ecom.sale.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentDto toDto(Payment payment);
}
