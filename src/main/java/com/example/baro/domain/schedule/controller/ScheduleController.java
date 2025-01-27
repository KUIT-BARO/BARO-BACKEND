package com.example.baro.domain.schedule.controller;

import com.example.baro.domain.schedule.dto.ScheduleRequest;
import com.example.baro.domain.schedule.dto.ScheduleResponse;
import com.example.baro.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/api/vi/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(
            summary = "시간표 등록",
            description = "시간표를 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "시간표 등록에 성공하였습니다."
    )
    @PostMapping
    public ResponseEntity<ScheduleResponse> registerSchedule(@RequestBody ScheduleRequest request) {
        ScheduleResponse response = scheduleService.registerSchedule(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "유저별 시간표 조회",
            description = "유저별 시간표 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저별 시간표 조회에 성공하였습니다."
    )

    @GetMapping("/{userId}")
    public ResponseEntity<List<ScheduleResponse>> getSchedule(@PathVariable Long userId) {
        return ResponseEntity.ok(scheduleService.getScheduleByUser(userId));
    }

    @Operation(
            summary = "시간표 삭제",
            description = "시간표를 삭제합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "시간표 삭제에 성공하였습니다."
    )
    @DeleteMapping("/{promiseId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long promiseId) {
        scheduleService.deleteSchedule(promiseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "시간표 수정",
            description = "시간표를 수정합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "시간표 에 성공하였습니다."
    )
    @PutMapping("/{houseId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(HouseUpdateRequest request) {
        ScheduleResponse response = scheduleService.updateSchedule(request.);
        return ResponseEntity.ok(response);
    }

}
