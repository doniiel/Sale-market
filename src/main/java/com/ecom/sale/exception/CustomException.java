package com.ecom.sale.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException {
    private final String api;
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp;

    public CustomException(String api, HttpStatus status, String message, LocalDateTime timestamp) {
        super(message);
        this.api = api;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
