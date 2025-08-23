package com.ecom.sale.util;

import com.ecom.sale.exception.CustomException;
import com.ecom.sale.model.Product;
import com.ecom.sale.repository.CategoryRepository;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class ValidatorUtils {

    public void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException("/product", HttpStatus.BAD_REQUEST, "Price must be >= 0", LocalDateTime.now());
        }
    }

    public void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new CustomException("/product", HttpStatus.BAD_REQUEST, "Quantity must be > 0", LocalDateTime.now());
        }
    }

    public void checkStock(Product product, int orderQuantity) {
        if (product.getQuantity() < orderQuantity) {
            throw new CustomException("/order", HttpStatus.BAD_REQUEST,
                    "Not enough stock for product: " + product.getName(),
                    LocalDateTime.now());
        }
    }

    public void validateAmount(BigDecimal expected, BigDecimal actual) {
        if (expected == null || actual == null || expected.compareTo(actual) != 0) {
            throw new CustomException("/payment", HttpStatus.BAD_REQUEST,
                    "Invalid payment amount. Expected=" + expected + ", Actual=" + actual,
                    LocalDateTime.now());
        }
    }

    public void ensureCategoryNameIsUnique(CategoryRepository repo, String name) {
        if (repo.existsByName(name)) {
            throw new CustomException("/category", HttpStatus.CONFLICT,
                    "Category with name=" + name + " already exists",
                    LocalDateTime.now());
        }
    }
}

