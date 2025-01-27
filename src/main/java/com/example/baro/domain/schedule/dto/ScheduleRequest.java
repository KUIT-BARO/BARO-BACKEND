package com.example.baro.domain.schedule.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleRequest {
    private Long id;

    private String name;

    private LocalDate date;

    private LocalTime time_start;

    private LocalTime time_end;

}
