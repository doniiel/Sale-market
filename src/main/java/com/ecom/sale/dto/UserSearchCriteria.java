package com.ecom.sale.dto;

import com.ecom.sale.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Schema(description = "Criteria for searching users")
public class UserSearchCriteria {

    @Schema(description = "Username filter (partial or exact match)", example = "daniyal")
    private String username;

    @Schema(description = "User role filter", example = "ROLE_ADMIN")
    private Role role;

    @Schema(description = "Email filter", example = "orynbek@example.com")
    private String email;

    @Schema(description = "Phone filter", example = "+77011234567")
    private String phone;
}