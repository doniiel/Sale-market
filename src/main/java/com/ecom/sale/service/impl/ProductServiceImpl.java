package com.ecom.sale.service.impl;

import com.ecom.sale.dto.ProductDto;
import com.ecom.sale.dto.ProductSearchCriteria;
import com.ecom.sale.dto.request.ProductRequest;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.mapper.ProductMapper;
import com.ecom.sale.model.Product;
import com.ecom.sale.repository.CategoryRepository;
import com.ecom.sale.repository.ProductRepository;
import com.ecom.sale.repository.specification.builder.ProductSpecificationBuilder;
import com.ecom.sale.service.ProductService;
import com.ecom.sale.util.UpdateUtils;
import com.ecom.sale.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    private final UpdateUtils updateUtils;

    private static final String API = "/products";

    @Override
    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        var category = getCategoryOrThrow(request.getCategoryId());
        validateProduct(request);

        var product = new Product();
        product.setCategory(category);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        productRepository.save(product);
        log.info("Created product: id={}, name='{}', category='{}', price={}, quantity={}",
                product.getId(), product.getName(), category.getName(), product.getPrice(), product.getQuantity());

        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductRequest request) {
        var product = getProductOrThrow(id);
        var category = getCategoryOrThrow(request.getCategoryId());
        validateProduct(request);

        if (!product.getCategory().getId().equals(category.getId())) {
            product.setCategory(category);
        }

        updateUtils.updateIfChanged(product::getName, product::setName, request.getName());
        updateUtils.updateIfChanged(product::getDescription, product::setDescription, request.getDescription());
        updateUtils.updateIfChanged(product::getPrice, product::setPrice, request.getPrice());
        updateUtils.updateIfChanged(product::getQuantity, product::setQuantity, request.getQuantity());

        productRepository.save(product);
        log.info("Updated product: id={}, name='{}', category='{}', price={}, quantity={}",
                product.getId(), product.getName(), category.getName(), product.getPrice(), product.getQuantity());

        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw exception("Product not found with id=" + id);
        }
        productRepository.deleteById(id);
        log.info("Deleted product: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        var product = getProductOrThrow(id);
        log.info("Fetched product: id={}, name='{}'", product.getId(), product.getName());
        return mapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(ProductSearchCriteria criteria, Pageable pageable) {
        var spec = new ProductSpecificationBuilder()
                .withName(criteria.getName())
                .withDescription(criteria.getDescription())
                .withCategory(criteria.getCategoryName())
                .withPriceFrom(criteria.getPriceFrom())
                .withPriceTo(criteria.getPriceTo())
                .withQuantityFrom(criteria.getQuantityFrom())
                .withQuantityTo(criteria.getQuantityTo())
                .build();

        var products = productRepository.findAll(spec, pageable).map(mapper::toDto);
        log.info("Fetched {} products with search criteria", products.getTotalElements());
        return products;
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> exception("Product not found with id=" + id));
    }

    private com.ecom.sale.model.Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> exception("Category not found with id=" + id));
    }

    private void validateProduct(ProductRequest request) {
        ValidatorUtils.validatePrice(request.getPrice());
        ValidatorUtils.validateQuantity(request.getQuantity());
    }

    private CustomException exception(String message) {
        return new CustomException(API, NOT_FOUND, message, LocalDateTime.now());
    }
}
