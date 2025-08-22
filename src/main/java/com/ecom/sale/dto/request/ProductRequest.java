package com.ecom.sale.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductRequest {

    private Long categoryId;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;
}
