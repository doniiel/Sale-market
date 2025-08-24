package com.ecom.sale.util;

import com.ecom.sale.dto.ErrorDto;
import com.ecom.sale.exception.CustomException;

public class ErrorUtils {

    private ErrorUtils() {}

    public static ErrorDto buildError(CustomException ex) {
        return ErrorDto.builder()
                .api(ex.getApi())
                .code(ex.getStatus().value())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .build();
    }
}