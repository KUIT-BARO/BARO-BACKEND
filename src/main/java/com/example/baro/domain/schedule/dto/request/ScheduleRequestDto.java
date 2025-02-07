package com.example.baro.domain.schedule.dto.request;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleRequestDto {

    private String name;

    private String dayOfWeek;

    private LocalTime timeStart;

    private LocalTime timeEnd;

}
