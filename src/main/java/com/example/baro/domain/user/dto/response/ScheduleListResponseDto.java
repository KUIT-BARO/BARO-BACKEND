package com.example.baro.domain.user.dto.response;

import lombok.Builder;

import java.time.DayOfWeek;
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
