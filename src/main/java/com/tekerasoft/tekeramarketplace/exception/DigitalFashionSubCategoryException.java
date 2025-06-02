package com.tekerasoft.tekeramarketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DigitalFashionSubCategoryException extends RuntimeException {
    public DigitalFashionSubCategoryException(String message) {
        super(message);
    }
}
