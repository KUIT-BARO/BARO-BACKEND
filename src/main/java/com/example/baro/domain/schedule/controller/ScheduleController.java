package com.example.baro.domain.schedule.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.schedule.dto.request.ScheduleRequestDto;
import com.example.baro.domain.schedule.dto.response.ScheduleListResponseDto;
import com.example.baro.domain.schedule.dto.response.ScheduleResponseDto;
import com.example.baro.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(
            summary = "시간표 등록",
            description = "시간표를 등록합니다."
    )
    @ApiResponse(
            responseCode = "20103",
            description = "시간표 등록에 성공하였습니다."
    )
    @PostMapping
    public ApiResponseDto<ScheduleResponseDto> registerSchedule(@RequestBody ScheduleRequestDto request,
                                                                @LoginUser User user) {
        ScheduleResponseDto response = scheduleService.registerSchedule(request, user);
        return ApiResponseDto.success(SuccessCode.SCHEDULE_REGISTER_SUCCESS, response);
    }

    @Operation(
            summary = "유저별 시간표 조회",
            description = "유저별 시간표 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "20005",
            description = "유저별 시간표 조회에 성공하였습니다."
    )

    @GetMapping("/{userId}")
    public ApiResponseDto<ScheduleListResponseDto> getScheduleByUser(@PathVariable Long userId) {
        ScheduleListResponseDto response = scheduleService.getScheduleByUser(userId);
        return ApiResponseDto.success(SuccessCode.SCHEDULE_GET_SUCCESS, response);
    }

    @Operation(
            summary = "나의 시간표 조회",
            description = "나의 시간표 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "20007",
            description = "나의 시간표 조회에 성공하였습니다."
    )

    @GetMapping
    public ApiResponseDto<ScheduleListResponseDto> getMYSchedule(@LoginUser User user) {
        ScheduleListResponseDto response = scheduleService.getMySchedule(user.getId());
        return ApiResponseDto.success(SuccessCode.MY_SCHEDULE_GET_SUCCESS, response);
    }

    @Operation(
            summary = "시간표 삭제",
            description = "시간표를 삭제합니다."
    )
    @ApiResponse(
            responseCode = "20402",
            description = "시간표 삭제에 성공하였습니다."
    )
    @DeleteMapping("/{promiseId}")
    public ApiResponseDto<Void> deleteSchedule(@PathVariable Long promiseId) {
        scheduleService.deleteSchedule(promiseId);
        return ApiResponseDto.success(SuccessCode.SCHEDULE_DELETE_SUCCESS);
    }

    @Operation(
            summary = "시간표 수정",
            description = "시간표를 수정합니다."
    )
    @ApiResponse(
            responseCode = "20006",
            description = "시간표 에 성공하였습니다."
    )
    @PutMapping("/{promiseId}")
    public ApiResponseDto<ScheduleResponseDto> updateSchedule(@PathVariable Long promiseId,
                                                              @RequestBody ScheduleRequestDto request) {
        ScheduleResponseDto response = scheduleService.updateSchedule(promiseId, request);
        return ApiResponseDto.success(SuccessCode.SCHEDULE_UPDATE_SUCCESS, response);
    }

}
