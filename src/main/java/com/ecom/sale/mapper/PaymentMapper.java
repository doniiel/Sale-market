package com.ecom.sale.mapper;

import com.ecom.sale.dto.PaymentDto;
import com.ecom.sale.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);
}
