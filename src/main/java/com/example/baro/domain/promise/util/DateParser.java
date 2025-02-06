package com.example.baro.domain.promise.util;

import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.domain.promise.exception.PromiseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String dateString) {

        try {
            return LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new PromiseException(ErrorCode.DATE_FORMAT_CONFLICT);
        }
    }
}
