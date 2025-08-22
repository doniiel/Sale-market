package com.ecom.sale.service.impl;

import com.ecom.sale.dto.CategoryDto;
import com.ecom.sale.dto.request.CategoryRequest;
import com.ecom.sale.mapper.CategoryMapper;
import com.ecom.sale.model.Category;
import com.ecom.sale.repository.CategoryRepository;
import com.ecom.sale.service.CategoryService;
import com.ecom.sale.util.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final UpdateUtils updateUtils;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryRequest request) {
        var category = new Category();
        validateName(request.getName());

        category.setName(Optional.ofNullable(request.getName()).orElse(""));
        category.setDescription(Optional.ofNullable(request.getDescription()).orElse(""));
        categoryRepository.save(category);
        log.info("Category created: {}", category);

        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        validateName(request.getName());

        updateUtils.updateIfChanged(category::getDescription, category::setDescription, request.getDescription());
        updateUtils.updateIfChanged(category::getName, category::setName, request.getName());
        categoryRepository.save(category);

        log.info("Category updated with id: " + id);
        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {;
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        log.info("Category with id: {} deleted from database", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String categoryName) {
        var category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + categoryName));
        return mapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    private void validateName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("Category with name " + name + " already exists");
        }
    }
}
