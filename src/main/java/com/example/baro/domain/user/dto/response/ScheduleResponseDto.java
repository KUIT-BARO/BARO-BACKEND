
package com.example.baro.domain.user.dto.response;

import com.example.baro.common.Enum.dayOfWeek.DayOfWeek;
import com.example.baro.common.entity.Schedule;
import com.example.baro.common.entity.User;

import java.time.LocalTime;

public record ScheduleResponseDto(
        Long id,
        User userId,
        String name,
        DayOfWeek dayOfWeek,
        LocalTime timeStart,
        LocalTime timeEnd
) {

    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUser(),
                schedule.getName(),
                schedule.getDayOfWeek(),
                schedule.getTimeStart(),
                schedule.getTimeEnd()
        );
    }
}