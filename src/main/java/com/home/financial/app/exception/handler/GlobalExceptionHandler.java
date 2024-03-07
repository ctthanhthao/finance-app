package com.home.financial.app.exception.handler;

import com.home.financial.app.exception.BaseException;
import com.home.financial.app.exception.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex,  HttpServletRequest re) {
        log.error("", ex);
        if (ex instanceof BadCredentialsException ||
        ex instanceof UsernameNotFoundException) {
            return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED, re);
        }
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN, re);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handlePSQLExceptions(PSQLException ex, HttpServletRequest re) {
        log.error("", ex);
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN, re);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtExceptions(JwtException ex, HttpServletRequest re) {
        log.error("", ex);
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN, re);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handlePSQLExceptions(BaseException ex, HttpServletRequest re) {
        log.error("", ex);
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN, re);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, HttpServletRequest re) {
        log.error("", ex);
        if (ex instanceof AccessDeniedException) {
            return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED, re);
        }
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            System.out.println("Content type = " + ((HttpMediaTypeNotSupportedException) ex).getContentType());
            ex.printStackTrace();
            return buildResponseEntity(ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE, re);
        }
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, re);
    }

    private ResponseEntity<Object> buildResponseEntity(Exception ex, HttpStatus httpStatus, HttpServletRequest re) {
        var apiError = ErrorResponse
                .builder()
                .path(re.getRequestURI())
                .status(httpStatus)
                .message(ex.getMessage())
                .errorClass(ex.getClass().getSimpleName())
                .build();
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
