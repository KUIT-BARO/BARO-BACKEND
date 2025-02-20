package com.example.baro.domain.user.dto.request;

import com.example.baro.common.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@NoArgsConstructor
@Getter
public class ScheduleRequestDto {

    private String name;

    private String dayOfWeek;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private String location;

}
