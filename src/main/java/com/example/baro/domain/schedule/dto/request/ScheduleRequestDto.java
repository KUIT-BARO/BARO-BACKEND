package com.example.baro.domain.schedule.dto.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleRequestDto {
    private Long id;

    private String name;

    private LocalDate date;

    private LocalTime time_start;

    private LocalTime time_end;

}
