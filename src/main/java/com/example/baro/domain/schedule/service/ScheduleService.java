package com.example.baro.domain.schedule.service;

import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.dto.enums.SuccessCode;

import com.example.baro.domain.promise.util.DateParser;
import com.example.baro.domain.schedule.dto.request.ScheduleRequestDto;
import com.example.baro.domain.schedule.dto.response.ScheduleResponseDto;
import com.example.baro.common.entity.Schedule;
import com.example.baro.domain.schedule.exception.ScheduleException;
import com.example.baro.domain.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    // private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto registerSchedule(ScheduleRequestDto request) {

        Schedule schedule = Schedule.builder()
                // .userId(scheduleId)
                .name(request.getName())
                .date(DateParser.parseDate(request.getDate()))
                .time_start(request.getTime_start())
                .time_end(request.getTime_end())
                .build();
        scheduleRepository.save(schedule);

        return ScheduleResponseDto.from(schedule);
    }

    public List<ScheduleResponseDto> getScheduleByUser(Long userId) {

        // isUserExist(userId);
        return scheduleRepository.findByUserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
    }

    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));

        scheduleRepository.delete(schedule);
    }

    public ScheduleResponseDto updateSchedule(ScheduleRequestDto request){
        Schedule schedule = Schedule.builder()
                // .userId(scheduleId)
                .name(request.getName())
                .date(DateParser.parseDate(request.getDate()))
                .time_start(request.getTime_start())
                .time_end(request.getTime_end())
                .build();
        scheduleRepository.update(schedule);
        return ScheduleResponseDto.from(schedule);
    }
}

