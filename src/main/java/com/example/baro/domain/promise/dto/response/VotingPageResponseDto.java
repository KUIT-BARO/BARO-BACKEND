package com.example.baro.domain.promise.dto.response;

import com.example.baro.common.Enum.status.Status;
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
            Long promisePersonalTimeId,
            LocalDate date,
            LocalTime timeStart,
            LocalTime timeEnd
    ){}

    @Builder
    public record PlaceDto(
            Long promisePersonalPlaceId,
            String placeName
    ){}

}

