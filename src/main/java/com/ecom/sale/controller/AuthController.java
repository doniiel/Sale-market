package com.ecom.sale.controller;

import com.ecom.sale.dto.request.ChangePasswordRequest;
import com.ecom.sale.dto.request.LoginRequest;
import com.ecom.sale.dto.request.RefreshTokenRequest;
import com.ecom.sale.dto.request.RegisterRequest;
import com.ecom.sale.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login user", description = "Authenticate user and return access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Parameter(description = "Login request payload", required = true)
            @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Parameter(description = "Register request payload", required = true)
            @RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Refresh access token", description = "Refresh JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @Parameter(description = "Refresh token request payload", required = true)
            @RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Operation(summary = "Change password", description = "Change user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Invalid request or current password incorrect")
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Change password request payload", required = true)
            @RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Logout user", description = "Invalidate user's refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully logged out")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

