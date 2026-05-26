package com.sistore.productservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class FieldValidationResponse {
    private Date timestamp;
    private int statusCode;
    private String url;
    private Map<String,String> errors;
}
