package com.home.financial.app.exception;

import org.springframework.http.HttpStatus;

public class EmailValidationException extends BaseException {
    public EmailValidationException() {
        super(ErrorCode
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Invalid email format.")
                .build());
    }
}
