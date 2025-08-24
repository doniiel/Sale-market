package com.ecom.sale.repository.specification.builder;

import com.ecom.sale.model.Category;
import com.ecom.sale.model.Product;
import com.ecom.sale.repository.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecificationBuilder {

    private Specification<Product> spec;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Integer quantityFrom;
    private Integer quantityTo;

    public ProductSpecificationBuilder withName(String name) {
        if (name != null && !name.isEmpty()) {
            spec = spec.and(ProductSpecification.hasName(name));
        }
        return this;
    }

    public ProductSpecificationBuilder withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            spec = spec.and(ProductSpecification.hasDescription(description));
        }
        return this;
    }

    public ProductSpecificationBuilder withCategory(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategory(categoryName));
        }
        return this;
    }

    public ProductSpecificationBuilder withPriceFrom(BigDecimal price) {
        if (price != null) {
            spec = spec.and(ProductSpecification.hasPriceFrom(price));
            this.priceFrom = price;
        }
        return this;
    }

    public ProductSpecificationBuilder withPriceTo(BigDecimal price) {
        if (price != null) {
            spec = spec.and(ProductSpecification.hasPriceTo(price));
            this.priceTo = price;
        }
        return this;
    }

    public ProductSpecificationBuilder withQuantityFrom(Integer quantity) {
        if (quantity != null) {
            spec = spec.and(ProductSpecification.hasQuantityFrom(quantity));
            this.quantityFrom = quantity;
        }
        return this;
    }

    public ProductSpecificationBuilder withQuantityTo(Integer quantity) {
        if (quantity != null) {
            spec = spec.and(ProductSpecification.hasQuantityTo(quantity));
            this.quantityTo = quantity;
        }
        return this;
    }

    public Specification<Product> build() {
        validatePriceRange();
        validateQuantityRange();
        return spec;
    }

    private void validatePriceRange() {
        if (priceFrom != null && priceTo != null && priceFrom.compareTo(priceTo) > 0) {
            throw new IllegalArgumentException("priceFrom must be less than or equal to priceTo");
        }
    }

    private void validateQuantityRange() {
        if (quantityFrom != null && quantityTo != null && quantityFrom.compareTo(quantityTo) > 0) {
            throw new IllegalArgumentException("quantityFrom must be less than or equal to quantityTo");
        }
    }
}
