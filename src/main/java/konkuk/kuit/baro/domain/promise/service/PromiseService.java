package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PendingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberSuggestStateDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.SuggestionProgress;
import konkuk.kuit.baro.domain.promise.dto.response.ConfirmedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseManagementResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.SuggestedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.VotingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.PromiseAvailableTimeRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseSuggestedPlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.BaseStatus;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.ColorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static konkuk.kuit.baro.domain.promise.dto.response.SuggestionProgress.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final PromiseMemberRepository promiseMemberRepository;
    private final UserRepository userRepository;
    private final PromiseAvailableTimeRepository promiseAvailableTimeRepository;
    private final PromiseSuggestedPlaceRepository promiseSuggestedPlaceRepository;

    private final ColorUtil colorUtil;

    @Transactional
    public void promiseSuggest(PromiseSuggestRequestDTO request, Long loginUserId) {

        // 약속 엔티티 생성
        Promise promise = Promise.builder()
                .promiseName(request.getPromiseName())
                .suggestedRegion(request.getSuggestedRegion())
                .suggestedStartDate(request.getSuggestedStartDate())
                .suggestedEndDate(request.getSuggestedEndDate())
                .build();

        // 약속 엔티티 저장
        Promise savedPromise = promiseRepository.save(promise);

        // 로그인한 유저 찾기
        User loginUser = findLoginUser(loginUserId);

        // 테두리 색상 설정
        String borderColor = colorUtil.generateRandomHexColor(savedPromise.getId());

        // 약속 참여자 엔티티 생성
        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, borderColor, loginUser, savedPromise);

        // 약속 참여자 엔티티 저장
        promiseMemberRepository.save(promiseMember);
    }

    // 약속 상태 확인
    public PromiseStatusResponseDTO getPromiseStatus(Long userId, Long promiseId) {
        Promise findPromise = findPromise(promiseId);
        PromiseMember findPromiseMember = findPromiseMember(userId, promiseId);

        return new PromiseStatusResponseDTO(findPromise.getStatus().name(), findPromiseMember.getIsHost());
    }

    // 약속 현황 조회 - 미정
    public PendingPromiseResponseDTO getPendingPromise(Long promiseId, Boolean isHost) {
        Promise findPromise = findPromise(promiseId);

        String promiseName = findPromise.getPromiseName();

        List<PromiseMemberSuggestStateDTO> promiseMembersSuggestState = getPromiseMembersSuggestStateList(promiseId);

        if (isHost) {
            return new PendingPromiseResponseDTO(promiseName, isVotingReady(promiseId), promiseMembersSuggestState);
        }

        return new PendingPromiseResponseDTO(promiseName, null, promiseMembersSuggestState);
    }

    private User findLoginUser(Long userId) {
        return userRepository.findById(userId)
    @Transactional
    public PromiseManagementResponseDTO getPromiseManagementData(Long loginUserId, boolean isHost) {
        User loginUser = findLoginUser(loginUserId);
        List<Promise> myPromiseList = promiseMemberRepository.findWithPromiseByUserId(loginUser.getId(), isHost);
        if( myPromiseList == null){myPromiseList = new ArrayList<>();}  // null일 경우 빈 리스트 반환

        List<SuggestedPromiseResponseDTO> suggestedPromises = new ArrayList<>();
        List<VotingPromiseResponseDTO> votingPromises = new ArrayList<>();
        List<ConfirmedPromiseResponseDTO> confirmedPromises = new ArrayList<>();

        for (Promise promise : myPromiseList) {
            switch (promise.getStatus()) {
                case PENDING -> suggestedPromises.add(mapToSuggestedPromiseDTO(promise));
                case VOTING -> votingPromises.add(mapToVotingPromiseDTO(promise));
                case CONFIRMED -> confirmedPromises.add(mapToConfirmedPromiseDTO(promise));
                case ACTIVE -> {}
                default -> throw new IllegalArgumentException();
            }
        }

        return new PromiseManagementResponseDTO(suggestedPromises, votingPromises, confirmedPromises);
    }

    private SuggestedPromiseResponseDTO mapToSuggestedPromiseDTO(Promise promise) {
        return new SuggestedPromiseResponseDTO(
                promise.getId(),
                promise.getPromiseName(),
                calculateDday(promise.getSuggestedEndDate()),
                promise.getSuggestedRegion(),
                promise.getSuggestedStartDate(),
                promise.getSuggestedEndDate()
        );
    }

    private VotingPromiseResponseDTO mapToVotingPromiseDTO(Promise promise) {
        return new VotingPromiseResponseDTO(
                promise.getId(),
                promise.getPromiseName(),
                calculateDday(promise.getSuggestedEndDate()),
                promise.getSuggestedRegion(),
                promise.getSuggestedStartDate(),
                promise.getSuggestedEndDate()
        );
    }

    private ConfirmedPromiseResponseDTO mapToConfirmedPromiseDTO(Promise promise) {
        return new ConfirmedPromiseResponseDTO(
                promise.getId(),
                promise.getPromiseName(),
                getPromiseMembersName(promise.getId()),
                promise.getPlace().getPlaceName(),
                promise.getFixedDate()
        );
    }

    private User findLoginUser(Long loginUserId) {
        return userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Promise findPromise(Long promiseId) {
        return promiseRepository.findById(promiseId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROMISE_NOT_FOUND));
    }

    private PromiseMember findPromiseMember(Long userId, Long promiseId) {

        if (!userIsExist(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (!promiseIsExist(promiseId)) {
            throw new CustomException(ErrorCode.PROMISE_NOT_FOUND);
        }

        PromiseMember findPromiseMember = promiseMemberRepository.findByUserIdAndPromiseId(userId, promiseId);

        if (findPromiseMember == null) {
            throw new CustomException(ErrorCode.PROMISE_MEMBER_NOT_FOUND);
        }

        return findPromiseMember;
    }

    private List<PromiseMember> findAllPromiseMembers(Long promiseId) {
        if (!promiseIsExist(promiseId)) {
            throw new CustomException(ErrorCode.PROMISE_NOT_FOUND);
        }

        return promiseMemberRepository.findAllByPromiseId(promiseId);
    }


    private boolean userIsExist(Long userId) {
        return userRepository.existsById(userId);
    }

    private boolean promiseIsExist(Long promiseId) {
        return promiseRepository.existsById(promiseId);
    }

    // 투표를 수행할 준비가 되었는지 체크
    private boolean isVotingReady(Long promiseId) {
        if (checkPromiseAvailableTimeIsExist(promiseId) && checkPromiseSuggestedPlaceIsExist(promiseId)) {
            return true;
        }

        return false;
    }

    // 특정 약속에 대해 약속 가능 시간 표시 내역이 존재하는지 체크
    private boolean checkPromiseAvailableTimeIsExist(Long promiseId) {
        return promiseAvailableTimeRepository.existsPromiseAvailableTimeByPromiseId(promiseId);
    }

    // 특정 약속에 대해 약속 장소 제안 내역이 존재하는지 체크
    private boolean checkPromiseSuggestedPlaceIsExist(Long promiseId) {
        return promiseSuggestedPlaceRepository.existsPromiseSuggestedPlaceByPromiseId(promiseId);
    }

    // 특정 약속 참여자가 약속 가능 시간 표시를 했는지 체크
    private boolean hasPromiseMemberCheckAvailableTime(Long promiseMemberId) {
        return promiseAvailableTimeRepository.existsPromiseAvailableTimeByPromiseMemberId(promiseMemberId);
    }

    // 특정 약속 참여자가 약속 장소 제안을 했는지 체크
    private boolean hasPromiseMemberSuggestPlace(Long promiseMemberId) {
        return promiseSuggestedPlaceRepository.existsPromiseSuggestedPlaceByPromiseMemberId(promiseMemberId);
    }

    // 특정 약속 참여자가 가능 시간 표시, 장소 제안을 했는지 체크
    private String extractSuggestionProgress(Long promiseMemberId) {
        boolean availableTime = hasPromiseMemberCheckAvailableTime(promiseMemberId);
        boolean suggestPlace = hasPromiseMemberSuggestPlace(promiseMemberId);

        if (availableTime && suggestPlace) {  // 시간 표시도 하고 장소 제안도 한 상황
            return COMPLETE.getValue();
        }

        if (!availableTime && !suggestPlace) {  // 시간 표시도 안하고 장소 제안도 안 한 상황
            return NONE.getValue();
        }

        return HALF.getValue();
    }

    private List<PromiseMemberSuggestStateDTO> getPromiseMembersSuggestStateList(Long promiseId) {
        return findAllPromiseMembers(promiseId).stream()
                .map(promiseMember -> new PromiseMemberSuggestStateDTO(
                        extractSuggestionProgress(promiseMember.getId()),
                        promiseMember.getIsHost(), promiseMember.extractProfileImage())
                ).toList();
    }


    private int calculateDday(LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    private List<String> getPromiseMembersName(Long promiseId) {
        List<String> memberNames = promiseMemberRepository.findMemberNamesByPromiseId(promiseId);
        return memberNames != null ? memberNames : new ArrayList<>();  // null일 경우 빈 리스트 반환
    }
}
