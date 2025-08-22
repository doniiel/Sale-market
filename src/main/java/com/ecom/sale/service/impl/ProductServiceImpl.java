package com.ecom.sale.service.impl;

import com.ecom.sale.dto.ProductDto;
import com.ecom.sale.dto.ProductSearchCriteria;
import com.ecom.sale.dto.request.ProductRequest;
import com.ecom.sale.mapper.ProductMapper;
import com.ecom.sale.model.Product;
import com.ecom.sale.repository.CategoryRepository;
import com.ecom.sale.repository.ProductRepository;
import com.ecom.sale.repository.specification.builder.ProductSpecificationBuilder;
import com.ecom.sale.service.ProductService;
import com.ecom.sale.util.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    private final UpdateUtils updateUtils;

    @Override
    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        var product = new Product();
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        validatePrice(request.getPrice());
        validateQuantity(request.getQuantity());

        product.setCategory(category);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        productRepository.save(product);

        log.info("Created product : {}", product);
        return mapper.toDto(product);

    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        validatePrice(request.getPrice());
        validateQuantity(request.getQuantity());

        updateUtils.updateIfChanged(product::getCategory, product::setCategory, category);
        updateUtils.updateIfChanged(product::getName, product::setName, request.getName());
        updateUtils.updateIfChanged(product::getPrice, product::setPrice, request.getPrice());
        updateUtils.updateIfChanged(product::getQuantity, product::setQuantity, request.getQuantity());
        productRepository.save(product);

        log.info("Updated product : {}", product);
        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Product with id: {} deleted from database", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        var product = productRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(ProductSearchCriteria criteria, Pageable pageable) {
        var spec = new ProductSpecificationBuilder()
                .withName(criteria.getName())
                .withDescription(criteria.getDescription())
                .withPriceFrom(criteria.getPriceFrom())
                .withPriceTo(criteria.getPriceTo())
                .withQuantityFrom(criteria.getQuantityFrom())
                .withQuantityTo(criteria.getQuantityTo())
                .build();

        return productRepository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Price cannot be negative");
        }
    }
}
