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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "투표 참여 여부 조회", description = "현재 로그인한 유저가 특정 약속에 대해 투표에 참여했는지 여부를 조회합니다.")
    @GetMapping("/{promiseId}/voting-status")
    @CustomExceptionDescription(PROMISE_MEMBER_HAS_VOTED)
    public BaseResponse<HasVotedResponseDTO> getHasVoted(@CurrentUserId Long userId,
                                                         @PathVariable("promiseId") Long promiseId) {
        return BaseResponse.ok(promiseService.getHasVoted(userId, promiseId));
    }


    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "투표 후보 목록 조회", description = "투표의 후보 목록을 조회합니다. 만약 투표를 하기 이전이라면 그냥 목록만 보이고, 투표를 한 이후 투표 확인, 혹은 수정을 하기 위해 진입한 경우 기존에 어떤 후보에 투표했었는지에 관한 정보를 함께 반환합니다.")
    @GetMapping("/{promiseId}/vote-candidate-list")
    @CustomExceptionDescription(VOTE_CANDIDATE_LIST)
    public BaseResponse<VoteCandidateListResponseDTO> getVoteCandidateList(@CurrentUserId Long userId,
                                                                           @PathVariable("promiseId") Long promiseId,
                                                                           @RequestParam("hasVoted") boolean hasVoted) {
        return BaseResponse.ok(promiseService.getVoteCandidateList(userId, promiseId, hasVoted));
    }


    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "투표 시작(개설)하기", description = "약속 가능 시간, 약속 제안 장소 데이터를 바탕으로 투표 목록을 생성, 및 투표를 시작(개설)합니다.")
    @PostMapping("/{promiseId}/init-vote")
    @CustomExceptionDescription(VOTE_INIT)
    public BaseResponse<Void> initVote(@PathVariable("promiseId") Long promiseId) {
        promiseService.initVote(promiseId);
        return BaseResponse.ok(null);
    }


}
