package com.minpay.Comiqueria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ChangePasswordRequiredException extends RuntimeException {
    public ChangePasswordRequiredException(String message) {
        super(message);
    }

    public ChangePasswordRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
