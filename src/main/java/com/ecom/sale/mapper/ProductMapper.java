package com.ecom.sale.mapper;

import com.ecom.sale.dto.ProductDto;
import com.ecom.sale.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);
}
