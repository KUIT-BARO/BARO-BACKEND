package com.example.baro.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationErrorResponseDto {

    private String field;
    private String message;
    private Object rejectedValue;
}
