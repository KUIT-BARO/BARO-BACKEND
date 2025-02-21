package com.example.baro.domain.user.service;

import com.example.baro.common.Enum.dayOfWeek.DayOfWeek;
import com.example.baro.common.dto.enums.ErrorCode;

import com.example.baro.common.entity.Place;
import com.example.baro.common.entity.User;
import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.user.dto.request.ScheduleRequestDto;
import com.example.baro.domain.user.dto.response.ScheduleListResponseDto;
import com.example.baro.domain.user.dto.response.ScheduleResponseDto;
import com.example.baro.common.entity.Schedule;
import com.example.baro.domain.user.exception.ScheduleException;
import com.example.baro.domain.user.repository.ScheduleRepository;
import com.example.baro.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public ScheduleResponseDto registerSchedule(ScheduleRequestDto request, User user) {

        Schedule schedule = Schedule.builder()
                .name(request.getName())
                .dayOfWeek(DayOfWeek.fromString(request.getDayOfWeek()))
                .timeStart(request.getTimeStart())
                .timeEnd(request.getTimeEnd())
                .user(user)
                .location(request.getLocation())
                .build();
        scheduleRepository.save(schedule);

        ScheduleResponseDto.ScheduleDto scheduleDto = ScheduleResponseDto.ScheduleDto.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .dayOfWeek(schedule.getDayOfWeek().toJavaDayOfWeek())
                .timeStart(schedule.getTimeStart())
                .timeEnd(schedule.getTimeEnd())
                .location(schedule.getLocation())
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
                        .dayOfWeek(schedule.getDayOfWeek().toJavaDayOfWeek())
                        .timeStart(schedule.getTimeStart())
                        .timeEnd(schedule.getTimeEnd())
                        .location(schedule.getLocation())
                        .build())
                .toList();

        return ScheduleListResponseDto.builder()
                .userId(userId)
                .schedules(scheduleDtos)
                .build();
    }

    public ScheduleListResponseDto getMySchedule(Long userId) {

        List<Schedule> schedules = scheduleRepository.findByUserId(userId);

        List<ScheduleListResponseDto.ScheduleDto> scheduleDtos = schedules.stream()
                .map(schedule -> ScheduleListResponseDto.ScheduleDto.builder()
                        .id(schedule.getId())
                        .name(schedule.getName())
                        .dayOfWeek(schedule.getDayOfWeek().toJavaDayOfWeek())
                        .timeStart(schedule.getTimeStart())
                        .timeEnd(schedule.getTimeEnd())
                        .location(schedule.getLocation())
                        .build())
                .toList();

        return ScheduleListResponseDto.builder()
                .userId(userId)
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
                DayOfWeek.fromString(request.getDayOfWeek()),
                request.getTimeStart(),
                request.getTimeEnd(),
                request.getLocation());

        ScheduleResponseDto.ScheduleDto scheduleDto = ScheduleResponseDto.ScheduleDto.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .dayOfWeek(schedule.getDayOfWeek().toJavaDayOfWeek())
                .timeStart(schedule.getTimeStart())
                .timeEnd(schedule.getTimeEnd())
                .location(schedule.getLocation())
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

    public void isScheduleExist(Long scheduleId) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}

