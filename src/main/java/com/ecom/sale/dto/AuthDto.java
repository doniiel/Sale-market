package com.ecom.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Schema(description = "DTO containing JWT tokens for authentication")
public class AuthDto {

    @Schema(description = "JWT Access Token. Used to access protected resources", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "JWT Refresh Token. Used to obtain a new Access Token", example = "dGhpc19pc19hX3NhbXBsZV9yZWZyZXNoX3Rva2Vu...")
    private String refreshToken;
}
