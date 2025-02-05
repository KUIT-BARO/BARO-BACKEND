package com.example.baro.domain.promise.service;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.entity.*;
import com.example.baro.common.exception.exceptionClass.CustomException;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.request.PromiseVoteRequestDto;
import com.example.baro.domain.promise.dto.response.PromiseConfirmResponseDto;
import com.example.baro.domain.promise.dto.response.PromiseSuggestResponseDto;
import com.example.baro.domain.promise.dto.response.PromiseViewResponseDto;
import com.example.baro.domain.promise.dto.response.VotingPageResponseDto;
import com.example.baro.domain.promise.exception.PromiseException;
import com.example.baro.domain.promise.repository.PromisePersonalTimeRepository;
import com.example.baro.domain.promise.repository.PromiseRepository;
import com.example.baro.domain.promise.util.DateParser;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import com.example.baro.domain.user.repository.UserPromiseRepository;
import com.example.baro.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final PlaceRepository placeRepository;
    private final UserPromiseRepository userPromiseRepository;
    private final PromisePersonalTimeRepository promisePersonalTimeRepository;
    private final LocalTime defaultStartTime = LocalTime.of(9, 0);
    private final LocalTime defaultEndTime = LocalTime.of(18, 0);
    private final PromisePersonalRepository promisePersonalRepository;

    public PromiseSuggestResponseDto registerPromise(PromiseSuggestRequestDto request) {
        Promise promise = Promise.builder()
                .name(request.getName())
                .dateStart(DateParser.parseDate(request.getDateStart()))
                .dateEnd(DateParser.parseDate(request.getDateEnd()))
                .timeStart(defaultStartTime)
                .timeEnd(defaultEndTime)
                .peopleNumber(request.getPeopleNumber())
                .purpose(PromisePurpose.fromString(request.getPurpose()))
                .build();

        promiseRepository.save(promise);

      return PromiseSuggestResponseDto.builder()
                .promiseId(promise.getId())
                .name(promise.getName())
                .purpose(promise.getPurpose())
                .dateStart(promise.getDateStart())
                .dateEnd(promise.getDateEnd())
                .place(promise.getPlace().getName())
                .peopleNumber(promise.getPeopleNumber()).build();
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
        List<Place> places = placeRepository.findAllByPromiseId(promiseId);

        List<VotingPageResponseDto.PromisePersonalTimeDto> promisePersonalTimeDtoList = personalTimes.stream()
                .map(promisePersonalTime -> VotingPageResponseDto.PromisePersonalTimeDto.builder()
                        .date(promisePersonalTime.getDate())
                        .timeStart(promisePersonalTime.getTimeStart())
                        .timeEnd(promisePersonalTime.getTimeEnd())
                        .build())
                .toList();

        List<VotingPageResponseDto.PlaceDto> pladeDtoList = places.stream()
                .map(place -> VotingPageResponseDto.PlaceDto.builder()
                        .placeId(place.getId())
                        .placeName(place.getName())
                        .build())
                .toList();

        return VotingPageResponseDto.builder()
                .promisePersonalTimeDtoList(promisePersonalTimeDtoList)
                .placeDtoList(pladeDtoList)
                .build();
    }

    public void votePromise(PromiseVoteRequestDto request, Long promiseId){
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        PromisePersonal promisePersonal = promisePersonalRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByPromiseId(promiseId)

        promisePersonal.vote();

        if(isReadyToConfirm(promisePersonals, promise.getPeopleNumber())){
            promise.confirm();
        }
    }

    public PromiseConfirmResponseDto getConfirmPromisePage(Long promiseId) {
        Promise promise = findPromiseById(promiseId);
        List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByPromiseId(promiseId)

        if (!isReadyToConfirm(promisePersonals, promise.getPeopleNumber())) {
            throw new PromiseException(ErrorCode.PROMISE_NOT_YET_VOTE_CONFLICT);
        }

        UserPromise userPromise = userPromiseRepository.findById(promise.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(promise.getPlace().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        PromiseConfirmResponseDto.PromiseConfirmDto promiseConfirmDto = PromiseConfirmResponseDto.PromiseConfirmDto.builder()
                .promiseId(promiseId)
                .name(promise.getName())
                .dateStart(promise.getDateStart())
                .dateEnd(promise.getDateEnd())
                .peopleNum(promise.getPeopleNumber())
                .purpose(promise.getPurpose())
                .placeName(place.getName())
                .UserName(userPromise.getUser().getNickname())
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
        List<Long> activePersonalPromiseIds = promisePersonalRepository.findActivePersonalPromiseIdsByPromiseId(promiseId);

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
}
