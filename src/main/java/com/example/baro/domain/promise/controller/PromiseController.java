package com.example.baro.domain.promise.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.response.PromiseSuggestResponseDto;
import com.example.baro.domain.promise.dto.response.PromiseViewResponseDto;
import com.example.baro.domain.promise.service.PromiseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Promise", description = "약속 제안서 관련 API. 토큰이 필요합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promise")
public class PromiseController {

    private final PromiseService promiseService;

    @Operation(
            summary = "약속 제안서 등록",
            description = "약속 제안서를 등록합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "약속 제안서 등록에 성공하였습니다."
    )
    @PostMapping("/suggest")
    public ResponseEntity<PromiseSuggestResponseDto> registerPromise(
            @RequestBody PromiseSuggestRequestDto request) {

        PromiseSuggestResponseDto response = promiseService.registerPromise(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "약속 제안서 삭제",
            description = "약속 제안서를 삭제합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "약속 제안서 삭제에 성공하였습니다."
    )
    @DeleteMapping("/suggest/{promiseId}")
    public ResponseEntity<Void> deletePromise(@PathVariable Long promiseId) {
        promiseService.deletePromise(promiseId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{promiseId}")
    public ApiResponseDto<PromiseViewResponseDto> getPromiseSuggestment(@PathVariable Long promiseId) {
        try {
            PromiseViewResponseDto promiseDto = promiseService.getPromiseSuggestmentById(promiseId);
            return ApiResponseDto.success(SuccessCode.PROMISE_GET_SUCCESS, promiseDto);
        } catch (EntityNotFoundException et){
            return ApiResponseDto.fail(ErrorCode.PROMISE_NOT_FOUND);
        } catch (Exception e) {
            return ApiResponseDto.fail(ErrorCode.SERVER_ERROR);
        }
    }

}
