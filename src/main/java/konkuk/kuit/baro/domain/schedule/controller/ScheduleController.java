package konkuk.kuit.baro.domain.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.baro.domain.schedule.dto.request.AddScheduleRequestDTO;
import konkuk.kuit.baro.domain.schedule.dto.response.GetSchedulesResponseDTO;
import konkuk.kuit.baro.domain.schedule.dto.response.SchedulesDTO;
import konkuk.kuit.baro.domain.schedule.service.ScheduleService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "일정 추가", description = "일정표에 일정을 추가합니다.")
    @PostMapping("")
    @CustomExceptionDescription(SCHEDULE_ADD)
    public BaseResponse<Void> addSchedule(@RequestBody @Validated AddScheduleRequestDTO req){
        scheduleService.addSchedule(req);
        return BaseResponse.ok(null);
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "일정 수정", description = "일정표의 일정을 수정합니다.")
    @PatchMapping("{scheduleId}")
    @CustomExceptionDescription(SCHEDULE_UPDATE)
    public BaseResponse<Void> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Validated AddScheduleRequestDTO req){
        scheduleService.updateSchedule(req, scheduleId);
        return BaseResponse.ok(null);
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "일정표 조회", description = "마이페이지에서 일정표를 조회합니다.")
    @GetMapping("")
    @CustomExceptionDescription(GET_SCHEDULES)
    public BaseResponse<GetSchedulesResponseDTO> getSchedules(){
        return BaseResponse.ok(scheduleService.getSchedules());
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "일정 삭제", description = "선택한 일정을 삭제합니다.")
    @DeleteMapping("{scheduleId}")
    public BaseResponse<Void> deleteSchedule(@PathVariable Long scheduleId){
        scheduleService.deleteSchedule(scheduleId);
        return BaseResponse.ok(null);
    }

    @Tag(name = "Promise Acceptance", description = "약속 수락 관련 API")
    @Operation(summary = "약속 참여자 일정표 조회", description = "약속 참여자들의 일정표를 조회합니다.")
    @GetMapping("{userId}")
    public BaseResponse<List<SchedulesDTO>> getPromiseMemberSchedules(@PathVariable Long userId){
        return BaseResponse.ok(scheduleService.getSchedulesByUserId(userId));
    }

}
