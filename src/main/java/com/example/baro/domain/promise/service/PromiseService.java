package com.example.baro.domain.promise.service;

import com.example.baro.common.exception.properties.ErrorCode;
import com.example.baro.domain.promise.dto.PromiseSuggestRequest;
import com.example.baro.domain.promise.dto.PromiseSuggestResponse;
import com.example.baro.domain.promise.entity.Promise;
import com.example.baro.domain.promise.exception.PromiseException;
import com.example.baro.domain.promise.repository.PromiseRepository;
import com.example.baro.domain.promise.util.DateParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@RequiredArgsConstructor
@Service
@Transactional
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final LocalTime defaultStartTime = LocalTime.of(9, 0);
    private final LocalTime defaultEndTime = LocalTime.of(18, 0);

    public PromiseSuggestResponse registerPromise(PromiseSuggestRequest request) {
        Promise promise = Promise.builder()
                .name(request.getName())
                .date_start(DateParser.parseDate(request.getDateStart()))
                .date_end(DateParser.parseDate(request.getDateEnd()))
                .time_start(defaultStartTime)
                .time_end(defaultEndTime)
                .peopleNum(request.getPeopleNum())
                .purpose(request.getPurpose())
                .build();

        promiseRepository.save(promise);

        return PromiseSuggestResponse.from(promise);
    }

    public void deletePromise(Long promiseId) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseException(ErrorCode.PROMISE_NOT_FOUND));

        promiseRepository.delete(promise);
    }

}
