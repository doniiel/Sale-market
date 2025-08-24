package com.ecom.sale.exception;


import com.ecom.sale.dto.ErrorDto;
import com.ecom.sale.util.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        var apiEx = new CustomException(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                String.join(", ", errors),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorUtils.buildError(apiEx));
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleApiException(CustomException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorUtils.buildError(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleOtherExceptions(WebRequest request,Exception ex) {
        var apiEx = new CustomException(
                request.getContextPath(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorUtils.buildError(apiEx));
    }
}
