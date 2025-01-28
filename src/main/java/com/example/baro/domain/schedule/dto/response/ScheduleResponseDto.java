
package com.example.baro.domain.schedule.dto.response;

import com.example.baro.domain.schedule.entity.Schedule;
import java.sql.Time;
import java.util.Date;

public record ScheduleResponseDto(
        Long id,
        Long userId,
        String name,
        Date date,
        Time timeStart,
        Time timeEnd
) {

    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUserId(),
                schedule.getName(),
                schedule.getDate(),
                schedule.getTimeStart(),
                schedule.getTimeEnd()
        );
    }
}