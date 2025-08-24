package com.ecom.sale.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterRequest {

    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone must not be empty")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone must contain only digits (10–15 digits)")
    private String phone;
}
