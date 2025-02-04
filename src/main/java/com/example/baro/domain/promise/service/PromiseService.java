package com.example.baro.domain.promise.service;

import com.example.baro.common.Enum.PromisePurpose.PromisePurpose;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.response.PromiseSuggestResponseDto;
import com.example.baro.common.entity.Promise;
import com.example.baro.domain.promise.dto.response.PromiseViewResponseDto;
import com.example.baro.domain.promise.exception.PromiseException;
import com.example.baro.domain.promise.repository.PromiseRepository;
import com.example.baro.domain.promise.util.DateParser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
@Transactional
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final LocalTime defaultStartTime = LocalTime.of(9, 0);
    private final LocalTime defaultEndTime = LocalTime.of(18, 0);

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

        // dto에서 fromEntity 말고 builder 패턴 쓰는게 좋아요.
        // 만약에 dto가 두 엔티티의 정보가 필요하면? 다른 api와 들어가는 로직이 다르면?
        // 어차피 이 api에 엔티티 변환 로직을 dto안에 넣은거면 한번밖에 안이용하는데 dto넣을 필요도 없어요
        // 객체지향적으로 lose coupling 해야됩니다. 어차피 아래 코드는 해당 api에만 있기 때문에 중복이 반복되는 것도 아니에요
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
}
