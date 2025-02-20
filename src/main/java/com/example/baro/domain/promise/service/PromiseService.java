package com.example.baro.domain.promise.service;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.entity.*;
import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.place.repository.SearchRepository;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.request.PromiseUserRequestDto;
import com.example.baro.domain.promise.dto.request.PromiseVoteRequestDto;
import com.example.baro.domain.promise.dto.response.*;
import com.example.baro.domain.promise.exception.PromiseException;
import com.example.baro.domain.promise.repository.PromisePersonalPlaceRepository;
import com.example.baro.domain.promise.repository.PromisePersonalTimeRepository;
import com.example.baro.domain.promise.repository.PromiseRepository;
import com.example.baro.domain.promise.repository.PromiseVoteRepository;
import com.example.baro.domain.promise.util.DateParser;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import com.example.baro.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.PersistenceContext;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PromiseService {

    private final UserRepository userRepository;
    private final PromiseRepository promiseRepository;
    private final PlaceRepository placeRepository;
    private final SearchRepository searchRepository;
    private final PromisePersonalTimeRepository promisePersonalTimeRepository;
    private final PromisePersonalPlaceRepository promisePersonalPlaceRepository;
    private final PromisePersonalRepository promisePersonalRepository;
    private final PromiseVoteRepository promiseVoteRepository;

    @Transactional
    public UserPlaceListResponseDto getUserPlace(Long userId) {
        List<Place> places = searchRepository.findPlacesByUserId(userId, PageRequest.of(0, 6));

        if (places == null || places.isEmpty()) {
            places = placeRepository.findRandomPlaces(PageRequest.of(0, 6));
        }

        List<UserPlaceListResponseDto.UserPlaceDto> placeDtoList = places.stream()
                .map(place -> UserPlaceListResponseDto.UserPlaceDto.builder()
                        .placeId(place.getId())
                        .address(place.getAddress())
                        .placeName(place.getName())
                        .build())
                .toList();

        return UserPlaceListResponseDto.builder()
                .userPlaceDto(placeDtoList)
                .build();
    }

    @Transactional
    public PromiseSuggestResponseDto registerPromise(PromiseSuggestRequestDto request, User user) {
        Place place = placeRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        Promise promise = Promise.builder()
                .name(request.getName())
                .dateStart(DateParser.parseDate(request.getDateStart()))
                .dateEnd(DateParser.parseDate(request.getDateEnd()))
                .peopleNumber(request.getPeopleNumber())
                .purpose(PromisePurpose.fromString(request.getPurpose()))
                .leaderName(user.getNickname())
                .place(place)
                .build();

        promiseRepository.save(promise);

        return PromiseSuggestResponseDto.builder()
                .promiseId(promise.getId())
                .name(promise.getName())
                .purpose(promise.getPurpose())
                .dateStart(promise.getDateStart())
                .dateEnd(promise.getDateEnd())
                .placeName(promise.getPlace().getName())
                .address(promise.getPlace().getAddress())
                .peopleNumber(promise.getPeopleNumber())
                .leaderName(promise.getLeaderName())
                .build();
    }

    @Transactional
    public void sharePromise(PromiseUserRequestDto request, Long promiseId) {

        // codeList에 있는 유저 조회
        List<User> users = userRepository.findByUserIdIn(request.getCodeList());

        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        // 조회된 유저 리스트를 바탕으로 PromisePersonal 객체 생성 및 저장
        List<PromisePersonal> promisePersonals = users.stream()
                .map(participant -> PromisePersonal.builder()
                        .promise(promise)
                        .user(participant)
                        .place(promise.getPlace())  // 필요하면 실제 Place 객체를 할당해야 함
                        .build())
                .collect(Collectors.toList());

        promisePersonalRepository.saveAll(promisePersonals);
    }

    @Transactional
    public void deletePromise(Long promiseId) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        promiseRepository.delete(promise);
    }

    @Transactional(readOnly = true)
    public PromiseViewResponseDto getPromiseSuggestmentById(Long promiseId) {
        Promise promise = promiseRepository.findById(promiseId).orElseThrow(EntityNotFoundException::new);
        return PromiseViewResponseDto.builder()
                .promiseId(promise.getId())
                .name(promise.getName())
                .purpose(promise.getPurpose())
                .dateStart(promise.getDateStart())
                .dateEnd(promise.getDateEnd())
                .place(promise.getPlace().getName())
                .peopleNumber(promise.getPeopleNumber()).build();
    }

    @Transactional
    public VotingPageResponseDto getVotingPromisePage(Long promiseId) {

        List<PromisePersonalTime> personalTimes = getOverlappingPersonalTimes(promiseId);
        List<PromisePersonalPlace> personalPlaces = getOverlappingPersonalPlaces(promiseId);

        List<VotingPageResponseDto.PromisePersonalTimeDto> timeDtoList = personalTimes.stream()
                .map(promisePersonalTime -> VotingPageResponseDto.PromisePersonalTimeDto.builder()
                        .promisePersonalTimeId(promisePersonalTime.getId())
                        .date(promisePersonalTime.getDate())
                        .timeStart(promisePersonalTime.getTimeStart())
                        .timeEnd(promisePersonalTime.getTimeEnd())
                        .build())
                .toList();


        List<VotingPageResponseDto.PlaceDto> placeDtoList = personalPlaces.stream()
                .map(personalPlace -> VotingPageResponseDto.PlaceDto.builder()
                        .promisePersonalPlaceId(personalPlace.getPlace().getId())
                        .placeName(personalPlace.getPlace().getName())
                        .build())
                .toList();

        return VotingPageResponseDto.builder()
                .promisePersonalTimeDtoList(timeDtoList)
                .placeDtoList(placeDtoList)
                .build();
    }

    public PromiseVoteResponseDto votePromise(PromiseVoteRequestDto request, Long promiseId) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        PromisePersonalTime promisePersonalTime = promisePersonalTimeRepository.findById(request.getPromisePersonalTimeId())
                .orElseThrow(() -> new PromiseException(ErrorCode.TIME_NOT_FOUND));

        PromisePersonalPlace promisePersonalPlace = promisePersonalPlaceRepository.findByPlaceId(request.getPromisePersonalPlaceId());

        PromiseVote promisevote = PromiseVote.builder()
                .promise(promise)
                .promisePersonalPlace(promisePersonalPlace)
                .promisePersonalTime(promisePersonalTime)
                .build();
        promiseVoteRepository.save(promisevote);


        List<PromiseVote> promiseVotes = promiseVoteRepository.findByPromiseId(promiseId);

        if (isReadyToConfirm(promiseVotes, promise.getPeopleNumber())) {

            PromisePersonalTime mostVotedTime = getMostVotedTime(promiseId);
            PromisePersonalPlace mostVotedPlace = getMostVotedPlace(promiseId);
            promise.confirm(mostVotedTime.getDate(), mostVotedTime.getTimeStart(), mostVotedTime.getTimeEnd(), mostVotedPlace.getPlace());
        }

        PromiseVoteResponseDto.PromiseVoteDto promiseVoteDto = PromiseVoteResponseDto.PromiseVoteDto.builder()
                .date(promisevote.getPromisePersonalTime().getDate())
                .timeStart(promisevote.getPromisePersonalTime().getTimeStart())
                .timeEnd(promisevote.getPromisePersonalTime().getTimeEnd())
                .placeName(promisevote.getPromisePersonalPlace().getPlace().getName())
                .status(promisevote.getStatus())
                .build();

        return PromiseVoteResponseDto.builder()
                .promiseVoteDto(promiseVoteDto)
                .build();
    }

    @Transactional
    public PromiseConfirmResponseDto getConfirmPromisePage(Long promiseId) {
        Promise promise = findPromiseById(promiseId);
        List<PromiseVote> promiseVotes = promiseVoteRepository.findByPromiseId(promiseId);

        if (!isReadyToConfirm(promiseVotes, promise.getPeopleNumber())) {
            throw new PromiseException(ErrorCode.PROMISE_NOT_YET_VOTE_CONFLICT);
        }

        Place place = placeRepository.findById(promise.getPlace().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        PromiseConfirmResponseDto.PromiseConfirmDto promiseConfirmDto = PromiseConfirmResponseDto.PromiseConfirmDto.builder()
                .promiseId(promiseId)
                .name(promise.getName())
                .date(promise.getDate())
                .peopleNum(promise.getPeopleNumber())
                .purpose(promise.getPurpose())
                .placeName(place.getName())
                .leaderName(promise.getLeaderName())
                .build();

        return PromiseConfirmResponseDto.builder()
                .promiseConfirmDto(promiseConfirmDto)
                .build();
    }

    @Transactional
    public List<PromisePersonalTime> getOverlappingPersonalTimes(Long promiseId) {
        Promise promise = findPromiseById(promiseId);
        List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByPromiseId(promiseId);

        // peopleNum과 ACTIVE한 personalPromise 수 비교
        if (!isReadyToVote(promisePersonals, promise.getPeopleNumber())) {
            throw new PromiseException(ErrorCode.PROMISE_NOT_YET_AGREE_CONFLICT);
        }

        // 활성화된 PersonalPromise에 속한 userId 가져오기
        List<Long> activePersonalPromiseIds = promisePersonalRepository.findActivePersonalPromiseIdsByPromiseId(promiseId, Status.ACTIVE);

        // 해당 PersonalPromise들의 모든 개인 시간(PromisePersonalTime) 조회
        List<PromisePersonalTime> personalTimes = promisePersonalTimeRepository.findByPromisePersonalIdIn(activePersonalPromiseIds);

        // 중복된 시간 필터링
        return findOverlappingPersonalTimes(personalTimes);
    }

    private List<PromisePersonalTime> findOverlappingPersonalTimes(List<PromisePersonalTime> personalTimes) {
        List<PromisePersonalTime> overlappingTimes = new ArrayList<>();

        for (int i = 0; i < personalTimes.size(); i++) {
            PromisePersonalTime current = personalTimes.get(i);

            for (int j = i + 1; j < personalTimes.size(); j++) {
                PromisePersonalTime other = personalTimes.get(j);

                if (isOverlapping(current, other)) {
                    if (!overlappingTimes.contains(current)) {
                        overlappingTimes.add(current);
                    }
                    if (!overlappingTimes.contains(other)) {
                        overlappingTimes.add(other);
                    }
                }
            }
        }

        return overlappingTimes;
    }

    private boolean isOverlapping(PromisePersonalTime p1, PromisePersonalTime p2) {
        // 날짜가 같아야만 시간이 겹치는지 비교
        if (!p1.getDate().equals(p2.getDate())) {
            return false;
        }

        // 시간이 겹치는지 확인
        return (p1.getTimeStart().isBefore(p2.getTimeEnd()) && p1.getTimeEnd().isAfter(p2.getTimeStart()));
    }

    public List<PromisePersonalPlace> getOverlappingPersonalPlaces(Long promiseId) {
        Promise promise = findPromiseById(promiseId);
        List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByPromiseId(promiseId);

        if (!isReadyToVote(promisePersonals, promise.getPeopleNumber())) {
            throw new PromiseException(ErrorCode.PROMISE_NOT_YET_AGREE_CONFLICT);
        }

        List<Long> activePersonalPromiseIds = promisePersonalRepository.findActivePersonalPromiseIdsByPromiseId(promiseId, Status.ACTIVE);
        List<PromisePersonalPlace> personalPlaces = promisePersonalPlaceRepository.findByPromisePersonal_IdIn(activePersonalPromiseIds);

        return findOverlappingPersonalPlaces(personalPlaces);
    }

    private List<PromisePersonalPlace> findOverlappingPersonalPlaces(List<PromisePersonalPlace> personalPlaces) {
        Map<String, Long> placeCountMap = new HashMap<>();

        // 장소별 선택 횟수 카운트
        for (PromisePersonalPlace place : personalPlaces) {
            placeCountMap.put(
                    place.getPlace().getName(),
                    placeCountMap.getOrDefault(place.getPlace().getName(), 0L) + 1
            );
        }

        // 2명 이상 선택한 장소 중 하나만 반환
        return personalPlaces.stream()
                .filter(place -> placeCountMap.get(place.getPlace().getName()) > 1) // 중복된 장소만 필터링
                .collect(Collectors.toMap(
                        place -> place.getPlace().getName(), // 키: 장소 이름
                        place -> place, // 값: 해당 장소 객체
                        (existing, replacement) -> existing // 중복 시 기존 값 유지
                ))
                .values()
                .stream()
                .toList();
    }

    private Promise findPromiseById(Long promiseId) {
        return promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));
    }

    public boolean isReadyToVote(List<PromisePersonal> promisePersonals, int peopleNumber) {
        long activeCount = promisePersonals.stream()
                .filter(p -> p.getStatus() == Status.ACTIVE)
                .count();
        return activeCount == peopleNumber;
    }

    public boolean isReadyToConfirm(List<PromiseVote> promiseVotes, int peopleNumber) {
        long activeCount = promiseVotes.stream()
                .filter(p -> p.getStatus() == Status.SUSPENDED)
                .count();
        return activeCount == peopleNumber;
    }

    private PromisePersonalTime getMostVotedTime(Long promiseId) {
        return promiseVoteRepository.findByPromiseId(promiseId).stream()
                .collect(Collectors.groupingBy(PromiseVote::getPromisePersonalTime, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private PromisePersonalPlace getMostVotedPlace(Long promiseId) {
        return promiseVoteRepository.findByPromiseId(promiseId).stream()
                .collect(Collectors.groupingBy(PromiseVote::getPromisePersonalPlace, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

    }
}
