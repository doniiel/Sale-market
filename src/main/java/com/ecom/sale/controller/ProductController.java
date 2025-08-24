package com.ecom.sale.controller;

import com.ecom.sale.dto.ProductDto;
import com.ecom.sale.dto.ProductSearchCriteria;
import com.ecom.sale.dto.request.ProductRequest;
import com.ecom.sale.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Operations for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create new product", description = "Creates a new product with the provided request data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Parameter(description = "Product creation request body", required = true)
            @RequestBody @Valid ProductRequest request
    ) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @Operation(summary = "Get product by ID", description = "Returns product details for the specified ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @Operation(summary = "Get all products", description = "Returns a paginated list of products filtered by search criteria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @ParameterObject
            @ModelAttribute ProductSearchCriteria criteria,
            @ParameterObject
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAllProducts(criteria, pageable));
    }

    @Operation(summary = "Update product", description = "Updates an existing product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Product update request body", required = true)
            @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(productService.updateProduct(id ,request));

    }

    @Operation(summary = "Delete product", description = "Deletes a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
