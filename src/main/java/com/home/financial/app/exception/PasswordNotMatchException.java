package com.home.financial.app.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException() {
        super(ErrorCode
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Old password is not correct.")
                .build());
    }
}
