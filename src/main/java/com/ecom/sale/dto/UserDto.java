package com.ecom.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "DTO representing user profile information")
public class UserDto {

    @Schema(description = "Unique user ID", example = "101")
    private Long id;

    @Schema(description = "Username of the user", example = "daniyal")
    private String username;

    @Schema(description = "Email address of the user", example = "orynbek@example.com")
    private String email;

    @Schema(description = "Phone number of the user", example = "+77011234567")
    private String phone;

    @Schema(description = "Short biography or user description", example = "Computer Science student at SDU")
    private String bio;
}
