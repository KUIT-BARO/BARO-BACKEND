package com.example.baro.domain.promise.controller;

import com.example.baro.domain.promise.dto.PromiseSuggestRequest;
import com.example.baro.domain.promise.dto.PromiseSuggestResponse;
import com.example.baro.domain.promise.service.PromiseService;
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
    public ResponseEntity<PromiseSuggestResponse> registerPromise(
            @RequestBody PromiseSuggestRequest request) {

        PromiseSuggestResponse response = promiseService.registerPromise(request);
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
}
