package konkuk.kuit.baro.domain.promise.service;

import jakarta.persistence.EntityManager;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseVoteRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PendingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberSuggestStateDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.ConfirmedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseManagementResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.SuggestedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.VotingPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.*;
import konkuk.kuit.baro.domain.promise.model.*;
import konkuk.kuit.baro.domain.promise.repository.*;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromisePlaceVoteHistoryRepository;
import konkuk.kuit.baro.domain.vote.repository.PromiseTimeVoteHistoryRepository;
import konkuk.kuit.baro.domain.vote.repository.PromiseVoteRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.util.ColorUtil;
import konkuk.kuit.baro.global.common.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static konkuk.kuit.baro.domain.promise.dto.response.SuggestionProgress.*;
import static konkuk.kuit.baro.global.common.response.status.BaseStatus.*;
import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseService {
    private final PromiseVoteRepository promiseVoteRepository;

    private final PromiseRepository promiseRepository;
    private final PromiseMemberRepository promiseMemberRepository;
    private final UserRepository userRepository;
    private final PromiseAvailableTimeRepository promiseAvailableTimeRepository;
    private final PromiseSuggestedPlaceRepository promiseSuggestedPlaceRepository;
    private final PromiseTimeVoteHistoryRepository promiseTimeVoteHistoryRepository;
    private final PromiseCandidateTimeRepository promiseCandidateTimeRepository;
    private final PromiseCandidatePlaceRepository promiseCandidatePlaceRepository;

    private final EntityManager em;

    private final ColorUtil colorUtil;
    private final PromisePlaceVoteHistoryRepository promisePlaceVoteHistoryRepository;

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
                case ACTIVE -> {
                }
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
            throw new CustomException(PROMISE_NOT_CONFIRMED);
        }

        return new PromiseStatusConfirmedPromiseResponseDTO(
                findPromise.getPromiseName(),
                fixedPlace.getPlaceName(),
                findPromise.extractFixedDateAndTime(),
                fixedPlace.getLocation().getY(),
                fixedPlace.getLocation().getX());
    }

    // 약속 제안 남은 시간 조회
    public PromiseSuggestRemainingTimeResponseDTO getPromiseSuggestRemainingTime(Long promiseId) {
        Promise findPromise = findPromise(promiseId);

        LocalDate suggestedEndDate = findPromise.getSuggestedEndDate();

        return new PromiseSuggestRemainingTimeResponseDTO(DateUtil.getRemainingTimeUntilEndDate(suggestedEndDate.plusDays(1).atTime(LocalTime.MIDNIGHT)));
    }

    // 투표 남은 시간 조회
    public PromiseVoteRemainingTimeResponseDTO getPromiseVoteRemainingTime(Long promiseId) {
        Promise findPromise = findPromise(promiseId);
        PromiseVote findPromiseVote = findPromiseVote(findPromise);

        return new PromiseVoteRemainingTimeResponseDTO(DateUtil.getRemainingTimeUntilEndDate(findPromiseVote.getVoteEndTime()));
    }

    // 특정 약속 참여자의 투표 참여 여부 반환
    public HasVotedResponseDTO getHasVoted(Long userId, Long promiseId) {
        Promise findPromise = findPromise(promiseId);
        PromiseMember findPromiseMember = findPromiseMember(userId, promiseId);
        PromiseVote findPromiseVote = findPromiseVote(findPromise);

        return new HasVotedResponseDTO(extractHasVoted(findPromiseVote, findPromiseMember));
    }

    // 투표 후보 목록 조회
    public VoteCandidateListResponseDTO getVoteCandidateList(Long userId, Long promiseId, boolean hasVoted) {
        Promise findPromise = findPromise(promiseId);
        PromiseVote findPromiseVote = findPromiseVote(findPromise);
        PromiseMember findPromiseMember = findPromiseMember(userId, promiseId);

        // 투표한 경우, 사용자가 선택한 약속 후보 시간 및 약속 후보 장소 ID 가져오기
        final Set<Long> selectedTimeIds = hasVoted
                ? findPromiseMember.getPromiseTimeVoteHistories().stream()
                .map(voteHistory -> voteHistory.getPromiseCandidateTime().getId())
                .collect(Collectors.toSet())
                : Collections.emptySet();

        final Set<Long> selectedPlaceIds = hasVoted
                ? findPromiseMember.getPromisePlaceVoteHistories().stream()
                .map(voteHistory -> voteHistory.getPromiseCandidatePlace().getId())
                .collect(Collectors.toSet())
                : Collections.emptySet();

        // 모든 후보 시간 리스트 생성 (투표 여부 반영)
        List<CandidateTimesDTO> candidateTimes = getCandidateTimesList(hasVoted, findPromiseVote, selectedTimeIds);

        // 모든 후보 장소 리스트 생성 (투표 여부 반영)
        List<CandidatePlacesDTO> candidatePlaces = getCandidatePlacesList(hasVoted, findPromiseVote, selectedPlaceIds);

        return new VoteCandidateListResponseDTO(candidateTimes, candidatePlaces);
    }


    // 투표 시작(개설)하기
    @Transactional
    public void initVote(Long promiseId) {
        // 투표 생성
        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now().plusDays(3))
                .build();

        PromiseVote savedVote = promiseVoteRepository.save(promiseVote);

        // 약속 정보 업데이트
        Promise findPromise = findPromise(promiseId);
        findPromise.setPromiseVote(savedVote);
        findPromise.setStatus(VOTING);

        // 약속 후보 시간 추출 - 약속 제안 시간에서 빈도수 기준 상위 3개 가져오기
        saveTop3CandidateTimes(promiseId, savedVote);

        // 약속 후보 장소 추출 - 약속 제안 장소에서 빈도수 기준 상위 3개 가져오기
        saveTop3CandidatePlaces(promiseId, savedVote);
    }


    @Transactional
    public void vote(Long userId, Long promiseId, PromiseVoteRequestDTO promiseVoteRequestDTO) {
        Promise findPromise = findPromise(promiseId);

        if (!findPromise.getStatus().equals(VOTING)) {
            throw new CustomException(PROMISE_VOTE_NOT_IN_PROGRESS);
        }

        PromiseMember findPromiseMember = findPromiseMember(userId, promiseId);
        PromiseVote findPromiseVote = findPromiseVote(findPromise);

        // 특정 유저의 기존 시간, 장소 투표 내역은 리셋
        promiseTimeVoteHistoryRepository.deleteAllByPromiseMember(findPromiseMember);
        promisePlaceVoteHistoryRepository.deleteAllByPromiseMember(findPromiseMember);


        // 시간 투표 내역 저장
        promiseVoteRequestDTO.getPromiseCandidateTimeIds()
                .stream()
                .map(promiseCandidateTimeId -> promiseCandidateTimeRepository.findById(promiseCandidateTimeId).orElseThrow(
                        () -> new CustomException(PROMISE_CANDIDATE_TIME_NOT_FOUND)
                ))
                .map(promiseCandidateTime -> PromiseTimeVoteHistory.createPromiseTimeVoteHistory(findPromiseVote, promiseCandidateTime, findPromiseMember))
                .forEach(promiseTimeVoteHistoryRepository::save);


        // 장소 투표 내역 저장
        promiseVoteRequestDTO.getPromiseCandidatePlaceIds()
                .stream()
                .map(promiseCandidatePlaceId -> promiseCandidatePlaceRepository.findById(promiseCandidatePlaceId).orElseThrow(
                        () -> new CustomException(PROMISE_CANDIDATE_PLACE_NOT_FOUND)
                ))
                .map(promiseCandidatePlace -> PromisePlaceVoteHistory.createPromisePlaceVoteHistory(promiseCandidatePlace, findPromiseVote, findPromiseMember))
                .forEach(promisePlaceVoteHistoryRepository::save);

    }


    private User findLoginUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private Promise findPromise(Long promiseId) {
        return promiseRepository.findById(promiseId)
                .orElseThrow(() -> new CustomException(PROMISE_NOT_FOUND));
    }

    private PromiseMember findPromiseMember(Long userId, Long promiseId) {

        if (!userIsExist(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }

        if (!promiseIsExist(promiseId)) {
            throw new CustomException(PROMISE_NOT_FOUND);
        }

        PromiseMember findPromiseMember = promiseMemberRepository.findByUserIdAndPromiseId(userId, promiseId);

        if (findPromiseMember == null) {
            throw new CustomException(PROMISE_MEMBER_NOT_FOUND);
        }

        return findPromiseMember;
    }

    private List<PromiseMember> findAllPromiseMembers(Long promiseId) {
        if (!promiseIsExist(promiseId)) {
            throw new CustomException(PROMISE_NOT_FOUND);
        }

        return promiseMemberRepository.findAllByPromiseId(promiseId);
    }

    private PromiseVote findPromiseVote(Promise promise) {
        PromiseVote findPromiseVote = promise.getPromiseVote();

        if (findPromiseVote == null) {
            throw new CustomException(PROMISE_VOTE_NOT_IN_PROGRESS);
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

    // 투표 후보 장소 목록 반환
    private List<CandidatePlacesDTO> getCandidatePlacesList(boolean hasVoted, PromiseVote findPromiseVote, Set<Long> selectedPlaceIds) {
        return findPromiseVote.getPromiseCandidatePlaces()
                .stream()
                .map(promiseCandidatePlace -> new CandidatePlacesDTO(
                        promiseCandidatePlace.getId(),
                        promiseCandidatePlace.getPlace().getPlaceName(),
                        hasVoted && selectedPlaceIds.contains(promiseCandidatePlace.getId()) // 투표했으면 true
                ))
                .toList();
    }

    // 투표 후보 시간 목록 반환
    private List<CandidateTimesDTO> getCandidateTimesList(boolean hasVoted, PromiseVote findPromiseVote, Set<Long> selectedTimeIds) {
        return findPromiseVote.getPromiseCandidateTimes()
                .stream()
                .map(promiseCandidateTime -> new CandidateTimesDTO(
                        promiseCandidateTime.getId(),
                        DateUtil.formatDate(promiseCandidateTime.getPromiseCandidateTimeDate()),
                        DateUtil.formatTime(promiseCandidateTime.getPromiseCandidateTimeStartTime()),
                        hasVoted && selectedTimeIds.contains(promiseCandidateTime.getId()) // 투표했으면 true
                ))
                .toList();
    }

    // 두 약속 가능 시간이 겹치는지 확인
    private boolean isOverlapping(PromiseAvailableTime base, PromiseAvailableTime other) {
        return base.getAvailableDate().isEqual(other.getAvailableDate()) &&
                !base.getAvailableEndTime().isBefore(other.getAvailableStartTime()) &&
                !base.getAvailableStartTime().isAfter(other.getAvailableEndTime());
    }

    // 상위 3개의 약속 가능 시간를 찾아서 약속 후보 시간으로 설정
    private void saveTop3CandidateTimes(Long promiseId, PromiseVote savedVote) {
        List<PromiseAvailableTime> allAvailableTimes = promiseAvailableTimeRepository.findAllByPromiseId(promiseId);

        if (allAvailableTimes.isEmpty()) {
            throw new CustomException(PROMISE_AVAILABLE_TIME_NOT_FOUND);
        }

        Map<PromiseAvailableTime, Integer> overlapCountMap = new ConcurrentHashMap<>();

        for (PromiseAvailableTime base : allAvailableTimes) {
            int count = 0;
            for (PromiseAvailableTime other : allAvailableTimes) {
                if (base == other) continue;
                if (isOverlapping(base, other)) {
                    count++;
                }
            }
            overlapCountMap.put(base, count);
        }

        overlapCountMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .map(pat -> PromiseCandidateTime.createPromiseCandidateTime(
                        pat.getAvailableDate(),
                        pat.getAvailableStartTime(),
                        savedVote
                ))
                .forEach(promiseCandidateTimeRepository::save);
    }

    // 상위 3개의 약속 제안 장소를 찾아서 약속 후보 장소로 설정
    private void saveTop3CandidatePlaces(Long promiseId, PromiseVote savedVote) {
        List<Place> top3PromiseSuggestedPlaces = promiseSuggestedPlaceRepository.findTopPlacesByPromiseId(promiseId, PageRequest.of(0, 3));

        if (top3PromiseSuggestedPlaces.isEmpty()) {
            throw new CustomException(PROMISE_SUGGESTED_PLACE_NOT_FOUND);
        }

        top3PromiseSuggestedPlaces.forEach(place -> {
            PromiseCandidatePlace candidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(savedVote, place);
            promiseCandidatePlaceRepository.save(candidatePlace);
        });
    }


}
