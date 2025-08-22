package com.ecom.sale.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductSearchCriteria {

    private String name;

    private String description;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private Integer quantityFrom;

    private Integer quantityTo;
}
