package com.home.financial.app.exception;

import org.springframework.http.HttpStatus;

public class UsernameExistsException extends BaseException {

    public UsernameExistsException() {
        super(ErrorCode
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Username already exists.")
                .build());
    }
}
