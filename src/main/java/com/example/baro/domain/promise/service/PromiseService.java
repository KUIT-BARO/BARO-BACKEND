package com.example.baro.domain.promise.service;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.entity.*;
import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.place.repository.SearchRepository;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.request.PromiseVoteRequestDto;
import com.example.baro.domain.promise.dto.response.*;
import com.example.baro.domain.promise.exception.PromiseException;
import com.example.baro.domain.promise.repository.PromisePersonalPlaceRepository;
import com.example.baro.domain.promise.repository.PromisePersonalTimeRepository;
import com.example.baro.domain.promise.repository.PromiseRepository;
import com.example.baro.domain.promise.util.DateParser;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import com.example.baro.domain.user.repository.UserPromiseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final PlaceRepository placeRepository;
    private final SearchRepository searchRepository;
    private final PromisePersonalTimeRepository promisePersonalTimeRepository;
    private final PromisePersonalPlaceRepository promisePersonalPlaceRepository;
    private final LocalTime defaultStartTime = LocalTime.of(9, 0);
    private final LocalTime defaultEndTime = LocalTime.of(18, 0);
    private final PromisePersonalRepository promisePersonalRepository;

    public UserPlaceListResponseDto getUserPlace(Long userId){
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

    public PromiseSuggestResponseDto registerPromise(PromiseSuggestRequestDto request, String userName) {
        Promise promise = Promise.builder()
                .name(request.getName())
                .dateStart(DateParser.parseDate(request.getDateStart()))
                .dateEnd(DateParser.parseDate(request.getDateEnd()))
                .peopleNumber(request.getPeopleNumber())
                .purpose(PromisePurpose.fromString(request.getPurpose()))
                .leaderName(userName)
                .build();

        promiseRepository.save(promise);

      return PromiseSuggestResponseDto.builder()
                .promiseId(promise.getId())
                .name(promise.getName())
                .purpose(promise.getPurpose())
                .dateStart(promise.getDateStart())
                .dateEnd(promise.getDateEnd())
                .placeName(promise.getPlace().getName())
                .peopleNumber(promise.getPeopleNumber())
                .leaderName(promise.getLeaderName())
              .build();
    }

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

    public VotingPageResponseDto getVotingPromisePage(Long promiseId) {

        List<PromisePersonalTime> personalTimes = getOverlappingPersonalTimes(promiseId);
        List<PromisePersonalPlace> places = getOverlappingPersonalPlaces(promiseId);

        List<VotingPageResponseDto.PromisePersonalTimeDto> promisePersonalTimeDtoList = personalTimes.stream()
                .map(promisePersonalTime -> VotingPageResponseDto.PromisePersonalTimeDto.builder()
                        .promisePersonalTimeId(promisePersonalTime.getId())
                        .date(promisePersonalTime.getDate())
                        .timeStart(promisePersonalTime.getTimeStart())
                        .timeEnd(promisePersonalTime.getTimeEnd())
                        .status(promisePersonalTime.getStatus())
                        .build())
                .toList();

        List<VotingPageResponseDto.PlaceDto> pladeDtoList = places.stream()
                .map(place -> VotingPageResponseDto.PlaceDto.builder()
                        .placeId(place.getId())
                        .placeName(place.getPlace().getName())
                        .status(place.getStatus())
                        .build())
                .toList();

        return VotingPageResponseDto.builder()
                .promisePersonalTimeDtoList(promisePersonalTimeDtoList)
                .placeDtoList(pladeDtoList)
                .build();
    }

    public PromiseVoteResponseDto votePromise(PromiseVoteRequestDto request, Long promiseId){
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        PromisePersonal promisePersonal = promisePersonalRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByPromiseId(promiseId);

        promisePersonal.vote();

        PromisePersonalTime promisePersonalTime = promisePersonalTimeRepository.findById(request.getPromisePersonalTimeId())
                .orElseThrow(() -> new PromiseException(ErrorCode.TIME_NOT_FOUND));
        promisePersonalTime.vote();

        PromisePersonalPlace promisePersonalPlace =  promisePersonalPlaceRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
        promisePersonalPlace.vote();

        if(isReadyToConfirm(promisePersonals, promise.getPeopleNumber())){

            PromisePersonalTime mostVotedTime = getMostVotedTime(promisePersonals);
            Place mostVotedPlace = getMostVotedPlace(promisePersonals).getPlace();

            promise.confirm(mostVotedTime.getDate(), mostVotedTime.getTimeStart(), mostVotedTime.getTimeEnd(),mostVotedPlace);
        }

        PromiseVoteResponseDto.PromiseVoteDto promiseVoteDto = PromiseVoteResponseDto.PromiseVoteDto.builder()
                .date(promisePersonalTime.getDate())
                .timeStart(promisePersonalTime.getTimeStart())
                .timeEnd(promisePersonalTime.getTimeEnd())
                .placeName(promisePersonalPlace.getPlace().getName())
                .status(promisePersonal.getStatus())
                .build();

        return PromiseVoteResponseDto.builder()
                .promiseVoteDto(promiseVoteDto)
                .build();
    }

    public PromiseConfirmResponseDto getConfirmPromisePage(Long promiseId) {
        Promise promise = findPromiseById(promiseId);
        List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByPromiseId(promiseId);

        if (!isReadyToConfirm(promisePersonals, promise.getPeopleNumber())) {
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

                if (isOverlapping(current, other) && !overlappingTimes.contains(current)) {
                    overlappingTimes.add(current);
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
        List<PromisePersonalPlace> personalPlaces = promisePersonalPlaceRepository.findByPromisePersonalIdIn(activePersonalPromiseIds);

        return findOverlappingPersonalPlaces(personalPlaces);
    }

    private List<PromisePersonalPlace> findOverlappingPersonalPlaces(List<PromisePersonalPlace> personalPlaces) {
        Map<String, Long> placeCountMap = new HashMap<>();

        for (PromisePersonalPlace place : personalPlaces) {
            placeCountMap.put(place.getPlace().getName(), placeCountMap.getOrDefault(place.getPlace().getName(), 0L) + 1);
        }

        return personalPlaces.stream()
                .filter(place -> placeCountMap.get(place.getPlace().getName()) > 1)
                .distinct()
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

    public boolean isReadyToConfirm(List<PromisePersonal> promisePersonals, int peopleNumber) {
        long activeCount = promisePersonals.stream()
                .filter(p -> p.getStatus() == Status.SUSPENDED)
                .count();
        return activeCount == peopleNumber;
    }

    private PromisePersonalTime getMostVotedTime(List<PromisePersonal> promisePersonals) {
        return promisePersonals.stream()
                .map(p -> promisePersonalTimeRepository.findByPromisePersonalId(p.getId()))
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(PromisePersonalTime::getVoteCount))
                .orElse(null);
    }

    private PromisePersonalPlace getMostVotedPlace(List<PromisePersonal> promisePersonals) {
        return promisePersonals.stream()
                .map(p -> promisePersonalPlaceRepository.findByPromisePersonalId(p.getId()))
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(PromisePersonalPlace::getVoteCount))
                .orElse(null);
    }

}
