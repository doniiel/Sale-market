package com.ecom.sale.dto;

import com.ecom.sale.model.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class ProductSearchCriteria {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private Category category;

    @DecimalMin(value = "0.0", message = "PriceFrom must be positive or zero")
    private BigDecimal priceFrom;

    @DecimalMin(value = "0.0", message = "PriceTo must be positive or zero")
    private BigDecimal priceTo;

    @Min(value = 0, message = "QuantityFrom must be positive or zero")
    private Integer quantityFrom;

    @Min(value = 0, message = "QuantityTo must be positive or zero")
    private Integer quantityTo;
}
