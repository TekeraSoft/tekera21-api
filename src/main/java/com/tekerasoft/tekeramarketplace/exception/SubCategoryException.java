package com.tekerasoft.tekeramarketplace.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubCategoryException extends RuntimeException {
    public SubCategoryException(String message) {
        super(message);
    }
}