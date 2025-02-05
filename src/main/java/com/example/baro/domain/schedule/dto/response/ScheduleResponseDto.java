package com.example.baro.domain.schedule.dto.response;

import com.example.baro.common.entity.Schedule;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
        LocalTime timeEnd
    ){}
}
