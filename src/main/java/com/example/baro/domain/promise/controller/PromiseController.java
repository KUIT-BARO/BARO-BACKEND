package com.example.baro.domain.promise.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.request.PromiseVoteRequestDto;
import com.example.baro.domain.promise.dto.response.*;
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
@RequestMapping("/promise")
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
    public ApiResponseDto<PromiseSuggestResponseDto> registerPromise(
            @RequestBody PromiseSuggestRequestDto request, @LoginUser User user) {

        PromiseSuggestResponseDto response = promiseService.registerPromise(request, user);
        return  ApiResponseDto.success(SuccessCode.PROMISE_GET_SUCCESS, response);
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
    public ApiResponseDto deletePromise(@PathVariable Long promiseId) {
        promiseService.deletePromise(promiseId);
        return ApiResponseDto.success(SuccessCode.UPCOMING_PROMISE_DELETE_SUCCESS);
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

    @Operation(
            summary = "투표 페이지 조회",
            description = "최종 약속 선정을 위한 투표 페이지를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "투표 페이지 조회에 성공하였습니다."
    )
    @GetMapping("/vote/{promiseId}")
    public ApiResponseDto<VotingPageResponseDto> getVotingPage(@PathVariable Long promiseId) {
        VotingPageResponseDto response = promiseService.getVotingPromisePage(promiseId);
        return ApiResponseDto.success(SuccessCode.PROMISE_VOTE_PAGE_GET_SUCCESS, response);
    }

    @Operation(
            summary = "투표 완료",
            description = "약속 선정을 위한 투표를 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "약속 선정 투표에 성공하였습니다."
    )
    @PostMapping("/vote/{promiseId}")
    public ApiResponseDto<PromiseVoteResponseDto> votePromise(@RequestBody PromiseVoteRequestDto request,
                                            @PathVariable Long promiseId) {
        PromiseVoteResponseDto response = promiseService.votePromise(request, promiseId);
        return ApiResponseDto.success(SuccessCode.PROMISE_VOTE_POST_SUCCESS, response);
    }

    @Operation(
            summary = "확정된 약속 조회",
            description = "확정된 약속을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "확정된 약속 조회에 성공하였습니다."
    )
    @GetMapping("/confirm/{promiseId}")
    public ApiResponseDto<PromiseConfirmResponseDto> getPromiseConfirmPage(@PathVariable Long promiseId) {
        PromiseConfirmResponseDto response = promiseService.getConfirmPromisePage(promiseId);
        return ApiResponseDto.success(SuccessCode.PROMISE_CONFIRM_PAGE_GET_SUCCESS, response);
    }

    @Operation(
            summary = "리뷰된 장소 조회",
            description = "리뷰된 장소를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "리뷰된 장소 조회에 성공하였습니다."
    )
    @GetMapping("/user-place")
    public ApiResponseDto<UserPlaceListResponseDto> getPromiseConfirmPage(@LoginUser User user) {
        UserPlaceListResponseDto response = promiseService.getUserPlace(user.getId());
        return ApiResponseDto.success(SuccessCode.USER_PLACE_GET_SUCCESS, response);
    }
}
