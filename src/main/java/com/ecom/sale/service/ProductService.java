package com.ecom.sale.service;

import com.ecom.sale.dto.ProductDto;
import com.ecom.sale.dto.ProductSearchCriteria;
import com.ecom.sale.dto.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto createProduct(ProductRequest request);

    ProductDto updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    ProductDto getProduct(Long id);

    Page<ProductDto> getAllProducts(ProductSearchCriteria criteria, Pageable pageable);
}
