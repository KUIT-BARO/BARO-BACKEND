package konkuk.kuit.baro.domain.promise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.request.SetPromiseAvailableTimeRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.*;
import konkuk.kuit.baro.domain.promise.dto.response.PendingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseAvailableTimeResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusResponseDTO;
import konkuk.kuit.baro.domain.promise.service.PromiseAvailableTimeService;
import konkuk.kuit.baro.domain.promise.service.PromiseService;
import konkuk.kuit.baro.global.auth.resolver.CurrentUserId;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import konkuk.kuit.baro.domain.promise.dto.response.VotingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.model.*;
import konkuk.kuit.baro.domain.promise.repository.*;
import konkuk.kuit.baro.domain.promise.service.PromiseAvailableTimeService;
import konkuk.kuit.baro.domain.promise.service.PromiseService;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromisePlaceVoteHistoryRepository;
import konkuk.kuit.baro.domain.vote.repository.PromiseTimeVoteHistoryRepository;
import konkuk.kuit.baro.domain.vote.repository.PromiseVoteRepository;
import konkuk.kuit.baro.global.auth.resolver.CurrentUserId;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import konkuk.kuit.baro.global.common.response.status.BaseStatus;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequestMapping("/promises")
@RequiredArgsConstructor
public class PromiseController {

    private final PromiseService promiseService;
    private final PromiseAvailableTimeService promiseAvailableTimeService;

    @Tag(name = "약속 제안 API", description = "약속 제안 관련 API")
    @Operation(summary = "약속 제안", description = "약속 이름, 날짜, 장소등을 입력하여 약속을 제안합니다.")
    @PostMapping
    @CustomExceptionDescription(PROMISE_SUGGEST)
    public BaseResponse<Void> suggestPromise(@Valid @RequestBody PromiseSuggestRequestDTO request,
                                             @CurrentUserId Long userId) {
        promiseService.promiseSuggest(request, userId);

        return BaseResponse.ok(null);
    }

    @Tag(name = "Promise Acceptance", description = "약속 수락 관련 API")
    @Operation(summary = "약속 수락 - 시간 선택 초기", description = "약속 시간 선택 초기 화면입니다.")
    @GetMapping("{promiseId}/time-choice")
    public BaseResponse<PromiseAvailableTimeResponseDTO> getPromiseAvailableTime(@PathVariable Long promiseId) {
        return BaseResponse.ok(promiseAvailableTimeService.getPromiseAvailableTime(promiseId));
    }

    @Tag(name = "Promise Acceptance", description = "약속 수락 관련 API")
    @Operation(summary = "약속 수락 - 시간 선택", description = "약속 참여자가 가능한 시간대를 선택합니다.")
    @PostMapping("{promiseId}/time-choice")
    @CustomExceptionDescription(SET_AVAILALBLE_TIME)
    public BaseResponse<Void> setPromiseAvailableTime(@PathVariable Long promiseId, Long userId,
                                                      @Validated @RequestBody SetPromiseAvailableTimeRequestDTO req) {
        promiseAvailableTimeService.setPromiseAvailableTime(req, userId, promiseId);
        return BaseResponse.ok(null);
    }

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "약속 상태 확인", description = "약속의 상태를 확인합니다.")
    @GetMapping("/{promiseId}/status")
    @CustomExceptionDescription(PROMISE_STATUS)
    public BaseResponse<PromiseStatusResponseDTO> getPromiseStatus(//@CurrentUserId Long userId,
                                                                   @PathVariable("promiseId") Long promiseId) {
        return BaseResponse.ok(promiseService.getPromiseStatus(1L, promiseId));
    }

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "약속 현황 - 미정", description = "약속 상태가 '미정'인 약속의 현황을 조회합니다.")
    @GetMapping("/{promiseId}/pending")
    @CustomExceptionDescription(PENDING_PROMISE_STATUS)
    public BaseResponse<PendingPromiseResponseDTO> getPendingPromise(@PathVariable("promiseId") Long promiseId,
                                                                     @RequestParam("isHost") Boolean isHost) {
        return BaseResponse.ok(promiseService.getPendingPromise(promiseId, isHost));
    }

    @Tag(name = "약속 관리 페이지 API", description = "약속 제안 관련 API")
    @Operation(summary = "약속 관리 페이지", description = "약속 관리 페이지에 필요한 데이터를 반환합니다.")
    @GetMapping("/management")
    @CustomExceptionDescription(PROMISE_MANAGING_PAGE)
    public BaseResponse<PromiseManagementResponseDTO> getPromiseManagementPage(
            @RequestParam(value = "isHost", required = true) boolean isHost
    ) {
        return BaseResponse.ok(promiseService.getPromiseManagementData(3L, isHost));
    }

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "약속 현황 - 투표중", description = "약속 상태가 '투표중'인 약속의 현황을 조회합니다.")
    @GetMapping("/{promiseId}/voting")
    @CustomExceptionDescription(VOTING_PROMISE_STATUS)
    public BaseResponse<PromiseStatusVotingPromiseResponseDTO> getVotingPromise(@PathVariable("promiseId") Long promiseId,
                                                                                @RequestParam("isHost") Boolean isHost) {
        return BaseResponse.ok(promiseService.getVotingPromise(promiseId, isHost));
    }

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "약속 현황 - 확정", description = "약속 상태가 '확정'인 약속의 현황을 조회합니다.")
    @GetMapping("/{promiseId}/confirmed")
    @CustomExceptionDescription(CONFIRMED_PROMISE_STATUS)
    public BaseResponse<PromiseStatusConfirmedPromiseResponseDTO> getConfirmedPromiseResponse(@PathVariable("promiseId") Long promiseId) {
        return BaseResponse.ok(promiseService.getConfirmedPromise(promiseId));
    }

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "약속 제안 남은 시간 조회", description = "약속 제안의 남은 시간을 조회합니다. 24시간 이상 남을 경우 D-N 형식, 24시간 미만으로 남을 경우 OO시 OO분 OO초 형식")
    @GetMapping("/{promiseId}/suggest-remaining-time")
    @CustomExceptionDescription(PROMISE_SUGGEST_REMAINING_TIME)
    public BaseResponse<PromiseSuggestRemainingTimeResponseDTO> getPromiseSuggestRemainingTimeResponse(@PathVariable("promiseId") Long promiseId) {
        return BaseResponse.ok(promiseService.getPromiseSuggestRemainingTime(promiseId));
    }

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "투표 남은 시간 조회", description = "투표 만료까지 남은 시간을 조회합니다. 24시간 이상 남을 경우 D-N 형식, 24시간 미만으로 남을 경우 OO시 OO분 OO초 형식")
    @GetMapping("/{promiseId}/vote-remaining-time")
    @CustomExceptionDescription(PROMISE_VOTE_REMAINING_TIME)
    public BaseResponse<PromiseVoteRemainingTimeResponseDTO> getPromiseVoteRemainingTimeResponse(@PathVariable("promiseId") Long promiseId) {
        return BaseResponse.ok(promiseService.getPromiseVoteRemainingTime(promiseId));
    }
}
