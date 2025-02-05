package com.example.baro.domain.schedule.service;

import com.example.baro.common.dto.enums.ErrorCode;

import com.example.baro.common.entity.User;
import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.domain.schedule.dto.request.ScheduleRequestDto;
import com.example.baro.domain.schedule.dto.response.ScheduleListResponseDto;
import com.example.baro.domain.schedule.dto.response.ScheduleResponseDto;
import com.example.baro.common.entity.Schedule;
import com.example.baro.domain.schedule.exception.ScheduleException;
import com.example.baro.domain.schedule.repository.ScheduleRepository;
import com.example.baro.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleResponseDto registerSchedule(ScheduleRequestDto request, User user) {

        Schedule schedule = Schedule.builder()
                .name(request.getName())
                .date(request.getDate())
                .timeStart(request.getTimeStart())
                .timeEnd(request.getTimeEnd())
                .user(user)
                .build();
        scheduleRepository.save(schedule);

        ScheduleResponseDto.ScheduleDto scheduleDto = ScheduleResponseDto.ScheduleDto.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .date(schedule.getDate())
                .timeStart(schedule.getTimeStart())
                .timeEnd(schedule.getTimeEnd())
                .build();

        return ScheduleResponseDto.builder()
                .schedule(scheduleDto)
                .build();
    }

    public ScheduleListResponseDto getScheduleByUser(Long userId) {

        isUserExist(userId);
        List<Schedule> schedules = scheduleRepository.findByUserId(userId);

        List<ScheduleListResponseDto.ScheduleDto> scheduleDtos = schedules.stream()
                .map(schedule -> ScheduleListResponseDto.ScheduleDto.builder()
                        .id(schedule.getId())
                        .name(schedule.getName())
                        .date(schedule.getDate())
                        .timeStart(schedule.getTimeStart())
                        .timeEnd(schedule.getTimeEnd())
                        .build())
                .toList();

        return ScheduleListResponseDto.builder()
                .schedules(scheduleDtos)
                .build();
    }

    public ScheduleListResponseDto getMySchedule(Long userId) {

        List<Schedule> schedules = scheduleRepository.findByUserId(userId);

        List<ScheduleListResponseDto.ScheduleDto> scheduleDtos = schedules.stream()
                .map(schedule -> ScheduleListResponseDto.ScheduleDto.builder()
                        .id(schedule.getId())
                        .name(schedule.getName())
                        .date(schedule.getDate())
                        .time_start(schedule.getTimeStart())
                        .time_end(schedule.getTimeEnd())
                        .build())
                .toList();

        return ScheduleListResponseDto.builder()
                .schedules(scheduleDtos)
                .build();
    }

    public void deleteSchedule(Long scheduleId) {

        Schedule schedule = returnScheduleExist(scheduleId);
        scheduleRepository.delete(schedule);
    }

    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto request){

       Schedule schedule = returnScheduleExist(scheduleId);

        schedule.update(request.getName(),
                request.getDayOfWeek(),
                request.getTimeStart(),
                request.getTimeEnd());
        scheduleRepository.save(schedule);

        ScheduleResponseDto.ScheduleDto scheduleDto = ScheduleResponseDto.ScheduleDto.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .
                .timeStart(schedule.getTimeStart())
                .timeEnd(schedule.getTimeEnd())
                .build();

        return ScheduleResponseDto.builder()
                .schedule(scheduleDto)
                .build();
    }

    public void isUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Schedule returnScheduleExist(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}

