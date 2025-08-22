package com.ecom.sale.dto.request;

import com.ecom.sale.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class OrderRequest {

    private OrderStatus status;

    private List<Long> productIds;

    private List<Integer> quantities;

}
