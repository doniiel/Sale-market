package com.ecom.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Error response structure")
public class ErrorDto {

    @Schema(description = "API endpoint where the error occurred", example = "/api/orders/15")
    private final String api;

    @Schema(description = "HTTP status code of the error", example = "404")
    private final int code;

    @Schema(description = "Detailed error message", example = "Order not found")
    private final String message;

    @Schema(description = "Timestamp when the error occurred", example = "2025-08-23T12:45:30")
    private final LocalDateTime timestamp;
}
