package com.example.baro.domain.schedule.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleListResponseDto (
    List<ScheduleDto> schedules
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
