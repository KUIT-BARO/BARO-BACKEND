package com.example.baro.domain.user.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ScheduleRequestDto {

    private String name;

    private String dayOfWeek;

    private LocalTime timeStart;

    private LocalTime timeEnd;

}
