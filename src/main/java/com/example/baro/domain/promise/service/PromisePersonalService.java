package com.example.baro.domain.promise.service;


import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.entity.*;
import com.example.baro.common.exception.exceptionClass.InvalidRequestException;
import com.example.baro.domain.keyword.repository.KeywordRepository;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.promise.dto.request.PromisePersonalKeywordRequestDto;
import com.example.baro.domain.promise.dto.request.PromisePersonalPlaceRequestDto;
import com.example.baro.domain.promise.dto.request.PromisePersonalTimeRequestDto;
import com.example.baro.domain.promise.repository.PromisePersonalKeywordRepository;
import com.example.baro.domain.promise.repository.PromisePersonalPlaceRepository;
import com.example.baro.domain.promise.repository.PromisePersonalTimeRepository;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
@Transactional
public class PromisePersonalService {
    final private PromisePersonalTimeRepository promisePersonalTimeRepository;
    final private PromisePersonalRepository promisePersonalRepository;
    final private PromisePersonalKeywordRepository promisePersonalKeywordRepository;
    final private KeywordRepository keywordRepository;
    final private PromisePersonalPlaceRepository promisePersonalPlaceRepository;
    final private PlaceRepository placeRepository;


    public void setPersonalTimes(@Valid PromisePersonalTimeRequestDto requestDto, User user, Long promiseId) {
        PromisePersonal promisePersonal = getPromisePersonal(user, promiseId);

        // PromisePersonalTime(개인 시간) 세팅
        for (PromisePersonalTimeRequestDto.TimeDto slot : requestDto.times()) {
            PromisePersonalTime timeSlot = PromisePersonalTime.builder()
                    .promisePersonal(promisePersonal)
                    .date(slot.date())  // LocalDate 자동 변환됨
                    .timeStart(slot.time_start())  // LocalTime 자동 변환됨
                    .timeEnd(slot.time_end())  // LocalTime 자동 변환됨
                    .build();

            promisePersonalTimeRepository.save(timeSlot);
        }
    }

    public void setPersonalKeywords(@Valid PromisePersonalKeywordRequestDto requestDto, User user, Long promiseId) {
        PromisePersonal promisePersonal = getPromisePersonal(user, promiseId);

        for (Long keywordId : requestDto.keywordIds()){
            // id로 키워드 가져오기
            Keyword keyword = keywordRepository
                    .findById(keywordId)
                    .orElseThrow(() -> new InvalidRequestException("잘못된 keywordId입니다. :  " + keywordId));

            // PromisePersonalKeyword(개인 키워드) 세팅
            PromisePersonalKeyword promisePersonalKeyword = PromisePersonalKeyword.builder()
                    .keyword(keyword)
                    .promisePersonal(promisePersonal)
                    .build();

            promisePersonalKeywordRepository.save(promisePersonalKeyword);

        }

    }

    public void setPersonalPlaces(@Valid PromisePersonalPlaceRequestDto requestDto, User user, Long promiseId) {
        PromisePersonal promisePersonal = getPromisePersonal(user, promiseId);

        for (Long placeId : requestDto.placeIds()){
            // id로 장소 가져오기
            Place place = placeRepository
                    .findById(placeId)
                    .orElseThrow(() -> new InvalidRequestException("잘못된 placeId입니다. :  " + placeId));

            // PromisePersonalPlace(개인 장소) 세팅
            PromisePersonalPlace promisePersonalplace = PromisePersonalPlace.builder()
                    .place(place)
                    .promisePersonal(promisePersonal)
                    .build();

            promisePersonalPlaceRepository.save(promisePersonalplace);

        }

        promisePersonal.setStatus(Status.ACTIVE);
    }

    public void rejectPersonal(User user, Long promiseId) {
        PromisePersonal promisePersonal = getPromisePersonal(user, promiseId);
        promisePersonal.setStatus(Status.SUSPENDED);
    }

    private PromisePersonal getPromisePersonal(User user, Long promiseId) {
        PromisePersonal promisePersonal = promisePersonalRepository
                .findByIdAAndUser(promiseId, user)
                .orElseThrow(() -> new InvalidRequestException("잘못된 promiseId입니다. :  " + promiseId));
        return promisePersonal;
    }
}