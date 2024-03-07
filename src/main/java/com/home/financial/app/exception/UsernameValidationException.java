package com.home.financial.app.exception;

import org.springframework.http.HttpStatus;

public class UsernameValidationException extends BaseException {
    public UsernameValidationException() {
        super(ErrorCode
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Invalid username.")
                .build());
    }
}
