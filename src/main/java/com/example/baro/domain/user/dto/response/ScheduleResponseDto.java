package com.example.baro.domain.user.dto.response;

import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record ScheduleResponseDto(
    ScheduleDto schedule
) {

    @Builder
    public record ScheduleDto(
        Long id,
        String name,
        DayOfWeek dayOfWeek,
        LocalTime timeStart,
        LocalTime timeEnd,
        String location
    ){}
}
