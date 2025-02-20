package com.example.baro.domain.promise.dto.request;


import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PromisePersonalTimeRequestDto(
        @NotNull(message = "times는 필수 입력값입니다.")
        @Size(min = 1, message = "최소 하나 이상의 시간을 입력해야 합니다.")
        List<TimeDto> times
) {
    public record TimeDto(
            LocalDate date,
            LocalTime time_start,
            LocalTime time_end
    ) {}
}