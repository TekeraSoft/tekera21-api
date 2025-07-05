package com.tekerasoft.tekeramarketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ThemeException extends RuntimeException {

    public ThemeException(String message) {
        super(message);
    }
}
