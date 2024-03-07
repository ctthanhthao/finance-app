package com.home.financial.app.exception;

public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    public BaseException(ErrorCode error) {
        super(error.getMessage());
        errorCode = error;
    }

    public BaseException(ErrorCode error, Throwable ex) {
        super(error.getMessage(), ex);
        errorCode = error;
    }
}
