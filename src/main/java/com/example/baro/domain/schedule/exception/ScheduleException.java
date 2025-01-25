package com.example.baro.domain.schedule.exception;

import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.common.exception.properties.ErrorCode;

public class ScheduleException extends CustomException {
    public ScheduleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
