package konkuk.kuit.baro.domain.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.baro.domain.schedule.dto.request.AddScheduleRequestDTO;
import konkuk.kuit.baro.domain.schedule.service.ScheduleService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/{scheduleId}")
    @CustomExceptionDescription(SCHEDULE_UPDATE)
    public BaseResponse<Void> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Validated AddScheduleRequestDTO req){
        scheduleService.updateSchedule(req, scheduleId);
        return BaseResponse.ok(null);
    }


}
