package com.ecom.sale.service.impl;

import com.ecom.sale.dto.CategoryDto;
import com.ecom.sale.dto.request.CategoryRequest;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.mapper.CategoryMapper;
import com.ecom.sale.model.Category;
import com.ecom.sale.repository.CategoryRepository;
import com.ecom.sale.repository.ProductRepository;
import com.ecom.sale.service.CategoryService;
import com.ecom.sale.util.UpdateUtils;
import com.ecom.sale.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper mapper;
    private final UpdateUtils updateUtils;

    private static final String API = "/category";

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryRequest request) {
        ValidatorUtils.ensureCategoryNameIsUnique(categoryRepository, request.getName());

        var category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);
        log.info("Created category: id={}, name='{}'", category.getId(), category.getName());

        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryRequest request) {
        final var category = categoryRepository.findById(id)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Category not found with id=" + id));

        if (!category.getName().equals(request.getName())) {
            ValidatorUtils.ensureCategoryNameIsUnique(categoryRepository, request.getName());
        }

        updateUtils.updateIfChanged(category::getName, category::setName, request.getName());
        updateUtils.updateIfChanged(category::getDescription, category::setDescription, request.getDescription());

        log.info("Updated category: id={}, name='{}'", id, category.getName());
        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Category not found with id=" + id));

        if (!productRepository.findAllByCategory_Id(id).isEmpty()) {
            throw exception(HttpStatus.BAD_REQUEST, "Cannot delete category with existing products");
        }

        categoryRepository.delete(category);
        log.info("Deleted category: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Category not found with id=" + id));
        log.info("Fetched category: id={}, name='{}'", category.getId(), category.getName());
        return mapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String categoryName) {
        var category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Category not found with name=" + categoryName));
        log.info("Fetched category by name: id={}, name='{}'", category.getId(), category.getName());
        return mapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        var categories = categoryRepository.findAll(pageable).map(mapper::toDto);
        log.info("Fetched categories: total={}", categories.getTotalElements());
        return categories;
    }

    private CustomException exception(HttpStatus status, String message) {
        return new CustomException(API, status, message, LocalDateTime.now());
    }
}
