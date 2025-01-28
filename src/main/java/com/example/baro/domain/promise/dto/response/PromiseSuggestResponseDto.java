package com.example.baro.domain.promise.dto.response;

import com.example.baro.domain.promise.entity.Promise;
import java.time.LocalDate;

public record PromiseSuggestResponseDto(
        Long promiseId,
        String name,
        LocalDate dateStart,
        LocalDate dateEnd,
        int peopleNum,
        String purpose
) {

    public static PromiseSuggestResponseDto from(Promise promise) {
        return new PromiseSuggestResponseDto(
                promise.getId(),
                promise.getName(),
                promise.getDate_start(),
                promise.getDate_end(),
                promise.getPeopleNum(),
                promise.getPurpose()
        );
    }
}