package com.example.baro.domain.schedule.service;

import com.example.baro.common.exception.properties.ErrorCode;
import com.example.baro.domain.schedule.dto.ScheduleResponse;
import com.example.baro.domain.schedule.entity.Schedule;
import com.example.baro.domain.schedule.exception.ScheduleException;
import com.example.baro.domain.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    /* private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public List<ScheduleResponse> getScheduleByUser(Long userId) {

        // isUserExist(userId);
        return scheduleRepository.findByUserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> registerSchedule(Long scheduleId) {

        Schedule schedule = Schedule.builder()
                .userId(scheduleId)
                .name()
                .date()
                .time_start()
                .time_end()
                .build();
        scheduleRepository.save(schedule);

        return Collections.singletonList(ScheduleResponse.from(schedule));
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public updateSchedule(Long ScheduleId){

    }

    private void isUserExist(Long userId) {
        Optional<User> user = scheduleRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new ScheduleException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void*/
}
