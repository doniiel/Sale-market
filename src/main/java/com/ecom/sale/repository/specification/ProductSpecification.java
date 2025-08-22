package com.ecom.sale.repository.specification;

import com.ecom.sale.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasDescription(String description) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Product> hasPriceFrom(BigDecimal price) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> hasPriceTo(BigDecimal price) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> hasQuantityFrom(Integer quantity) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("quantity"), quantity);

    }

    public static Specification<Product> hasQuantityTo(Integer quantity) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("quantity"), quantity);
    }
}
