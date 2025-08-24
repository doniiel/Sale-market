package com.ecom.sale.controller;

import com.ecom.sale.dto.CategoryDto;
import com.ecom.sale.dto.request.CategoryRequest;
import com.ecom.sale.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "Operations for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create new category", description = "Creates a new category with the given request body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody @Valid CategoryRequest request
    ) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @Operation(summary = "Get category by ID", description = "Returns category details for the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(
            @Parameter(description = "ID of the category to retrieve", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @Operation(summary = "Get category by name", description = "Returns category details for the given name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/by-name")
    public ResponseEntity<CategoryDto> getCategoryByName(
            @Parameter(description = "Category name", required = true)
            @RequestParam(required = true) String name
    ) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));

    }

    @Operation(summary = "Get all categories", description = "Returns a paginated list of all categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAllCategory(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(categoryService.getAllCategory(pageable));
    }

    @Operation(summary = "Update category", description = "Updates an existing category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category successfully updated"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Delete category", description = "Deletes an existing category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true)
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
