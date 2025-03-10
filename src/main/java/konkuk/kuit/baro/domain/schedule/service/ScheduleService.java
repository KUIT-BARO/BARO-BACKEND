package konkuk.kuit.baro.domain.schedule.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.baro.domain.schedule.dto.request.AddScheduleRequestDTO;
import konkuk.kuit.baro.domain.schedule.model.Schedule;
import konkuk.kuit.baro.domain.schedule.repository.ScheduleRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addSchedule(AddScheduleRequestDTO req) {
        if(req.getScheduleName().length() > 12){
            throw new CustomException(INVALID_SCHEDULE_NAME);
        }
        if(req.getStartTime().equals(req.getEndTime())){
            throw new CustomException(INVALID_SCHEDULE_TIME);
        }
        User loginUser = userRepository.findById(1L).orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedule(loginUser.getId(),
                req.getDayOfWeekEnum(),
                req.getStartTime(),
                req.getEndTime());
        if(!overlappingSchedules.isEmpty()){
            throw new CustomException(SCHEDULE_CONFLICT);
        }
        Schedule schedule = Schedule.createSchedule(
                req.getScheduleName(),
                req.getDayOfWeekEnum(),
                req.getStartTime(),
                req.getEndTime(),
                req.getPlaceName(),
                loginUser
                );
        scheduleRepository.save(schedule);
    }
}
