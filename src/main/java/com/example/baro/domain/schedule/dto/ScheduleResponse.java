package com.example.baro.domain.schedule.dto;

import com.example.baro.domain.schedule.entity.Schedule;
import lombok.Builder;
import lombok.Getter;
import java.sql.Time;
import java.util.Date;

@Getter
@Builder
public class ScheduleResponse {

    private Long id;

    private Long userId;

    private String name;

    private Date date;

    private Time time_start;

    private Time time_end;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .userId(schedule.getUserId())
                .name(schedule.getName())
                .date(schedule.getDate())
                .time_start(schedule.getTime_start())
                .time_end(schedule.getTime_end())
                .build();
    }
}
