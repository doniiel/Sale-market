package com.ecom.sale.mapper;

import com.ecom.sale.dto.CategoryDto;
import com.ecom.sale.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);
}
