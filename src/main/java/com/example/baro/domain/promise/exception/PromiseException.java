package com.example.baro.domain.promise.exception;

import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.common.dto.enums.ErrorCode;

public class PromiseException extends CustomException {
    public PromiseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
