package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PendingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberSuggestStateDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.ConfirmedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseManagementResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.SuggestedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.VotingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.*;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.*;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromiseTimeVoteHistoryRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.ColorUtil;
import konkuk.kuit.baro.global.common.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    private final PromiseTimeVoteHistoryRepository promiseTimeVoteHistoryRepository;

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

    @Transactional
    public PromiseManagementResponseDTO getPromiseManagementData(Long loginUserId, boolean isHost) {
        User loginUser = findLoginUser(loginUserId);
        List<Promise> myPromiseList = promiseRepository.findByUserIdAndHost(loginUser.getId(), isHost)
                .orElse(new ArrayList<>());

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
                DateUtil.calculateDday(promise.getSuggestedEndDate()),
                promise.getSuggestedRegion(),
                promise.getSuggestedStartDate(),
                promise.getSuggestedEndDate()
        );
    }

    private VotingPromiseResponseDTO mapToVotingPromiseDTO(Promise promise) {
        return new VotingPromiseResponseDTO(
                promise.getId(),
                promise.getPromiseName(),
                DateUtil.calculateDday(promise.getSuggestedEndDate()),
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

    // 약속 현황 조회 - 투표중
    public PromiseStatusVotingPromiseResponseDTO getVotingPromise(Long promiseId, Boolean isHost) {
        Promise findPromise = findPromise(promiseId);

        String promiseName = findPromise.getPromiseName();

        PromiseVote findPromiseVote = findPromiseVote(findPromise);

        // 한 명의 유저라도 투표를 했으면 됨. 이 때 시간, 장소 투표는 한 번에 수행되기 때문에, 약속 시간 투표내역, 약속 장소 투표내역 둘 중 하나라도 존재하면 종료 가능
        List<PromiseMemberVoteStateDTO> promiseMemberVoteState = getPromiseMemberVoteStateList(promiseId, findPromiseVote);

        if (isHost) {
            return new PromiseStatusVotingPromiseResponseDTO(promiseName, canCloseVoting(findPromiseVote.getId()), promiseMemberVoteState);
        }

        return new PromiseStatusVotingPromiseResponseDTO(promiseName, null, promiseMemberVoteState);
    }

    // 약속 현황 조회 - 확정
    public PromiseStatusConfirmedPromiseResponseDTO getConfirmedPromise(Long promiseId) {
        Promise findPromise = findPromise(promiseId);

        Place fixedPlace = findPromise.getPlace();
        LocalDate fixedDate = findPromise.getFixedDate();
        LocalTime fixedTime = findPromise.getFixedTime();

        if (fixedPlace == null || fixedDate == null || fixedTime == null) {
            throw new CustomException(ErrorCode.PROMISE_NOT_CONFIRMED);
        }

        return new PromiseStatusConfirmedPromiseResponseDTO(
                findPromise.getPromiseName(),
                fixedPlace.getPlaceName(),
                findPromise.extractFixedDateAndTime(),
                fixedPlace.getLocation().getY(),
                fixedPlace.getLocation().getX());
    }

    // 약속 현황 조회 - 확정
    public ConfirmedPromiseResponseDTO getConfirmedPromise(Long promiseId) {
        Promise findPromise = findPromise(promiseId);

        Place fixedPlace = findPromise.getPlace();
        LocalDate fixedDate = findPromise.getFixedDate();
        LocalTime fixedTime = findPromise.getFixedTime();

        if (fixedPlace == null || fixedDate == null || fixedTime == null) {
            throw new CustomException(ErrorCode.PROMISE_NOT_CONFIRMED);
        }

        return new ConfirmedPromiseResponseDTO(
                findPromise.getPromiseName(),
                fixedPlace.getPlaceName(),
                findPromise.extractFixedDateAndTime(),
                fixedPlace.getLocation().getY(),
                fixedPlace.getLocation().getX());
    }


    private User findLoginUser(Long userId) {
        return userRepository.findById(userId)
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

    private PromiseVote findPromiseVote(Promise promise) {
        PromiseVote findPromiseVote = promise.getPromiseVote();

        if (findPromiseVote == null) {
            throw new CustomException(ErrorCode.PROMISE_VOTE_NOT_STARTED);
        }

        return findPromiseVote;
    }


    private boolean userIsExist(Long userId) {
        return userRepository.existsById(userId);
    }

    private boolean promiseIsExist(Long promiseId) {
        return promiseRepository.existsById(promiseId);
    }

    private boolean voteHistoryIsExist(Long promiseVoteId) {
        return promiseTimeVoteHistoryRepository.existsByPromiseVoteId(promiseVoteId);
    }

    // 투표를 수행할 준비가 되었는지 체크
    private boolean isVotingReady(Long promiseId) {
        if (checkPromiseAvailableTimeIsExist(promiseId) && checkPromiseSuggestedPlaceIsExist(promiseId)) {
            return true;
        }

        return false;
    }

    // 투표를 종료할 수 있는지 체크
    private boolean canCloseVoting(Long promiseVoteId) {
        if (voteHistoryIsExist(promiseVoteId)) {  // 투표 내역이 존재
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

    // 특정 약속 참여자가 투표에 참여했는지를 체크
    private boolean extractHasVoted(PromiseVote promiseVote, PromiseMember promiseMember) {
        return promiseTimeVoteHistoryRepository.existsByPromiseVoteAndPromiseMember(promiseVote, promiseMember);
    }

    // 약속 참여자들의 제안 여부 조회
    private List<PromiseMemberSuggestStateDTO> getPromiseMembersSuggestStateList(Long promiseId) {
        return findAllPromiseMembers(promiseId).stream()
                .map(promiseMember -> new PromiseMemberSuggestStateDTO(
                        extractSuggestionProgress(promiseMember.getId()),
                        promiseMember.getIsHost(), promiseMember.extractProfileImage())
                ).toList();
    }

    private List<String> getPromiseMembersName(Long promiseId) {
        return promiseMemberRepository.findMemberNamesByPromiseId(promiseId)
                .orElseGet(Collections::emptyList);
    }

    // 약속 참여자들의 투표 여부 조회
    private List<PromiseMemberVoteStateDTO> getPromiseMemberVoteStateList(Long promiseId, PromiseVote promiseVote) {
        return findAllPromiseMembers(promiseId).stream()
                .map(promiseMember -> new PromiseMemberVoteStateDTO(
                        extractHasVoted(promiseVote, promiseMember),
                        promiseMember.getIsHost(),
                        promiseMember.extractProfileImage()
                )).toList();
    }

}
