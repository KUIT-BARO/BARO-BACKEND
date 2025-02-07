package com.example.baro.domain.schedule.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record ScheduleListResponseDto (
        Long userId,
        List<ScheduleDto> schedules
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
