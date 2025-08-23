package com.ecom.sale.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductSearchCriteria {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "PriceFrom must be positive or zero")
    private BigDecimal priceFrom;

    @DecimalMin(value = "0.0", inclusive = true, message = "PriceTo must be positive or zero")
    private BigDecimal priceTo;

    @Min(value = 0, message = "QuantityFrom must be positive or zero")
    private Integer quantityFrom;

    @Min(value = 0, message = "QuantityTo must be positive or zero")
    private Integer quantityTo;
}
