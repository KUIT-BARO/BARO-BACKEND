package com.example.baro.domain.promise.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record VotingPageResponseDto (
        List<PromisePersonalTimeDto> promisePersonalTimeDtoList,
        List<PlaceDto> placeDtoList
){
    @Builder
    public record PromisePersonalTimeDto (
            LocalDate date,
            LocalTime timeStart,
            LocalTime timeEnd
    ){}

    @Builder
    public record PlaceDto(
            String placeName
    ){}

}

