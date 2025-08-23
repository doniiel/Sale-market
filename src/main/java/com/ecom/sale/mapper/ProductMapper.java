package com.ecom.sale.mapper;

import com.ecom.sale.dto.ProductDto;
import com.ecom.sale.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "category.name")
    ProductDto toDto(Product product);
}
