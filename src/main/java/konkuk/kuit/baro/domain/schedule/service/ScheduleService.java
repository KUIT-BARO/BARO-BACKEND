package konkuk.kuit.baro.domain.schedule.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.baro.domain.schedule.dto.request.AddScheduleRequestDTO;
import konkuk.kuit.baro.domain.schedule.dto.response.GetSchedulesResponseDTO;
import konkuk.kuit.baro.domain.schedule.dto.response.SchedulesDTO;
import konkuk.kuit.baro.domain.schedule.model.DayOfWeek;
import konkuk.kuit.baro.domain.schedule.model.Schedule;
import konkuk.kuit.baro.domain.schedule.repository.ScheduleRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
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
        User loginUser = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        validateNewSchedule(req, loginUser);

        Schedule schedule = Schedule.createSchedule(
                req.getScheduleName(),
                DayOfWeek.ofCode(req.getDayOfWeek()),
                req.getStartTime(),
                req.getEndTime(),
                req.getPlaceName(),
                loginUser
        );

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void updateSchedule(AddScheduleRequestDTO req, Long scheduleId) {
        User loginUser = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));

        validateUpdatedSchedule(req, loginUser, scheduleId);

        Schedule.setSchedule(
                existingSchedule,
                req.getScheduleName(),
                DayOfWeek.ofCode(req.getDayOfWeek()),
                req.getStartTime(),
                req.getEndTime(),
                req.getPlaceName()
        );
    }

    private void validateNewSchedule(AddScheduleRequestDTO req, User loginUser) {
        validateCommonScheduleRules(req);

        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedule(
                loginUser.getId(),
                DayOfWeek.ofCode(req.getDayOfWeek()),
                req.getStartTime(),
                req.getEndTime()
        );

        if (!overlappingSchedules.isEmpty()) {
            throw new CustomException(SCHEDULE_CONFLICT);
        }
    }

    private void validateUpdatedSchedule(AddScheduleRequestDTO req, User loginUser, Long scheduleId) {
        validateCommonScheduleRules(req);

        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedulesExcludingId(
                loginUser.getId(),
                DayOfWeek.ofCode(req.getDayOfWeek()),
                req.getStartTime(),
                req.getEndTime(),
                scheduleId
        );

        if (!overlappingSchedules.isEmpty()) {
            throw new CustomException(SCHEDULE_CONFLICT);
        }
    }

    private void validateCommonScheduleRules(AddScheduleRequestDTO req) {
        if (req.getScheduleName().length() > 25) {
            throw new CustomException(INVALID_SCHEDULE_NAME);
        }
        if (req.getStartTime().equals(req.getEndTime())) {
            throw new CustomException(INVALID_SCHEDULE_TIME);
        }
    }

    public GetSchedulesResponseDTO getSchedules() {
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<SchedulesDTO> schedules = scheduleRepository.findAllByUserId(loginUser.getId());
        if(schedules.isEmpty()){
            throw new CustomException(SCHEDULE_NOT_EXISTS);
        }
        return new GetSchedulesResponseDTO(
                loginUser.getProfileImage(),
                loginUser.getName(),
                loginUser.getEmail(),
                schedules
        );
    }
}

