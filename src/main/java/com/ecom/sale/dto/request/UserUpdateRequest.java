package com.ecom.sale.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserUpdateRequest {

    private String bio;

    private String email;

    private String phone;

}
