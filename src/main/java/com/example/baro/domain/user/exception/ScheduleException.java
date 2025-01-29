package com.example.baro.domain.user.exception;

import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.common.dto.enums.ErrorCode;

public class ScheduleException extends CustomException {
    public ScheduleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
