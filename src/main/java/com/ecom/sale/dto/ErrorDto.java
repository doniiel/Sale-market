package com.ecom.sale.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorDto {

    private final String api;

    private final int code;

    private final String message;

    private final LocalDateTime timestamp;
}
