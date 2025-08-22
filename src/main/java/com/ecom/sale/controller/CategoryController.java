package com.ecom.sale.controller;

import com.ecom.sale.dto.CategoryDto;
import com.ecom.sale.dto.request.CategoryRequest;
import com.ecom.sale.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody @Valid CategoryRequest request
    ) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<CategoryDto> getCategoryByName(
            @RequestParam(required = true) String name
    ) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));

    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAllCategory(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(categoryService.getAllCategory(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
