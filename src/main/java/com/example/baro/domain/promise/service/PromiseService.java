package com.example.baro.domain.promise.service;

import com.example.baro.common.Enum.PromisePurpose.PromisePurpose;
import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.entity.PromisePersonal;
import com.example.baro.domain.promise.dto.request.PromiseSuggestRequestDto;
import com.example.baro.domain.promise.dto.response.PromiseSuggestResponseDto;
import com.example.baro.common.entity.Promise;
import com.example.baro.domain.promise.exception.PromiseException;
import com.example.baro.domain.promise.repository.PromiseRepository;
import com.example.baro.domain.promise.util.DateParser;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final PromisePersonalRepository promisePersonalRepository;
    private final LocalTime defaultStartTime = LocalTime.of(9, 0);
    private final LocalTime defaultEndTime = LocalTime.of(18, 0);

    public PromiseSuggestResponseDto registerPromise(PromiseSuggestRequestDto request) {
        Promise promise = Promise.builder()
                .name(request.getName())
                .dateStart(DateParser.parseDate(request.getDateStart()))
                .dateEnd(DateParser.parseDate(request.getDateEnd()))
                .timeStart(defaultStartTime)
                .timeEnd(defaultEndTime)
                .peopleNumber(request.getPeopleNum())
                .purpose(PromisePurpose.fromString(request.getPurpose()))
                .build();

        promiseRepository.save(promise);

        return PromiseSuggestResponseDto.from(promise);
    }

    public void deletePromise(Long promiseId) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        promiseRepository.delete(promise);
    }
}
