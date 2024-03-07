package com.home.financial.app.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends BaseException {
    public UsernameNotFoundException() {
        super(ErrorCode
                .builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Username could not be found.")
                .build());
    }
}
