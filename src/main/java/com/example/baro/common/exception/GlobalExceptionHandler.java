package com.example.baro.common.exception;


import com.example.baro.common.exception.dto.ErrorResponse;
import com.example.baro.common.exception.dto.ValidationErrorResponseDto;
import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.common.dto.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 사용자 정의 에러를 보여주는 함수
    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(
            CustomException e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getRuntimeValue());
    }

    // 바인딩, 검증 에러를 보여주는 함수
    @ExceptionHandler(value = {
            BindException.class,
            MethodArgumentNotValidException.class
    })
    protected ResponseEntity<List<ValidationErrorResponseDto>> validationException(BindException e,
                                                                                   HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationErrorResponseDto> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ValidationErrorResponseDto error = new ValidationErrorResponseDto(
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            );
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    // 이외에 기타 에러(500)를 보여주는 함수
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(
            Exception e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(ErrorCode.SERVER_ERROR, e.getMessage());
    }
}
