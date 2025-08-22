package com.ecom.sale.service;

import com.ecom.sale.dto.CategoryDto;
import com.ecom.sale.dto.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDto createCategory(CategoryRequest request);

    CategoryDto updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

    CategoryDto getCategory(Long id);

    CategoryDto getCategoryByName(String categoryName);

    Page<CategoryDto> getAllCategory(Pageable pageable);

}
