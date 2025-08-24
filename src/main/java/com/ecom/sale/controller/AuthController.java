package com.ecom.sale.controller;

import com.ecom.sale.dto.AuthDto;
import com.ecom.sale.dto.request.ChangePasswordRequest;
import com.ecom.sale.dto.request.LoginRequest;
import com.ecom.sale.dto.request.RefreshTokenRequest;
import com.ecom.sale.dto.request.RegisterRequest;
import com.ecom.sale.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API (JWT + Refresh Token)")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login user",
            description = "Authenticate user with username and password and return JWT tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = @Content(schema = @Schema(implementation = AuthDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(
            @Parameter(description = "Login request payload", required = true)
            @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Register user",
            description = "Register a new user in the system."
    )
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

    @Operation(
            summary = "Refresh access token",
            description = "Generate a new access token using a valid refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed",
                    content = @Content(schema = @Schema(implementation = AuthDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthDto> refreshToken(
            @Parameter(description = "Refresh token request payload", required = true)
            @RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Operation(
            summary = "Change password",
            description = "Change the current user's password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Invalid request or wrong current password")
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Change password request payload", required = true)
            @RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Logout user",
            description = "Invalidate the user's refresh token and clear security context."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully logged out")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}