package konkuk.kuit.baro.domain.promise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.request.SetPromiseAvailableTimeRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PendingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseAvailableTimeResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusResponseDTO;
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
import org.locationtech.jts.geom.Coordinate;
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

    @Tag(name = "약속 현황 API", description = "약속 현황 관련 API")
    @Operation(summary = "약속 현황 - 투표중", description = "약속 상태가 '투표중'인 약속의 현황을 조회합니다.")
    @GetMapping("/{promiseId}/voting")
    @CustomExceptionDescription(VOTING_PROMISE_STATUS)
    public BaseResponse<VotingPromiseResponseDTO> getVotingPromise(@PathVariable("promiseId") Long promiseId,
                                                                   @RequestParam("isHost") Boolean isHost) {
        return BaseResponse.ok(promiseService.getVotingPromise(promiseId, isHost));
    }
}
