package com.sistore.productservice.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends RuntimeException{

    HttpStatus status;

    public ProductNotFoundException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}
