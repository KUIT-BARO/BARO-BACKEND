package com.example.baro.domain.schedule.dto.response;

import com.example.baro.common.entity.Schedule;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleResponseDto(
    ScheduleDto schedule
) {

    @Builder
    public record ScheduleDto(
        Long id,
        String name,
        LocalDate date,
        LocalTime time_start,
        LocalTime time_end
    ){}
}
