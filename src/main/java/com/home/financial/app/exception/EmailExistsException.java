package com.home.financial.app.exception;

import org.springframework.http.HttpStatus;

public class EmailExistsException extends BaseException {
    public EmailExistsException() {
        super(ErrorCode
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Email already exists.")
                .build());
    }
}
