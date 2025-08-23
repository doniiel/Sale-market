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
import org.webjars.NotFoundException;

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
        ensureCategoryNameIsUnique(request.getName());

        var category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);
        log.info("Category created: {}", category);

        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!category.getName().equals(request.getName())) {
            ensureCategoryNameIsUnique(request.getName());
        }

        updateUtils.updateIfChanged(category::getName, category::setName, request.getName());
        updateUtils.updateIfChanged(category::getDescription, category::setDescription, request.getDescription());

        categoryRepository.save(category);
        log.info("Category updated with id: {} ", id);

        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category not found with id=" + id);
        }
        categoryRepository.deleteById(id);
        log.info("Deleted category with id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + categoryName));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    private void ensureCategoryNameIsUnique(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category with name=" + name + " already exists");
        }
    }
}
