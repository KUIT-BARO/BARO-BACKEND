package com.example.baro.domain.promise.dto.response;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import com.example.baro.common.entity.Promise;
import java.time.LocalDate;

public record PromiseSuggestResponseDto(
        Long promiseId,
        String name,
        LocalDate dateStart,
        LocalDate dateEnd,
        int peopleNum,
        PromisePurpose purpose
) {

    public static PromiseSuggestResponseDto from(Promise promise) {
        return new PromiseSuggestResponseDto(
                promise.getId(),
                promise.getName(),
                promise.getDateStart(),
                promise.getDateEnd(),
                promise.getPeopleNumber(),
                promise.getPurpose()
        );
    }
}