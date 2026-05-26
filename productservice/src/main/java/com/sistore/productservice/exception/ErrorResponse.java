package com.sistore.productservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private Date timestamp;
    private int statusCode;
    private String url;
    private String message;
}
