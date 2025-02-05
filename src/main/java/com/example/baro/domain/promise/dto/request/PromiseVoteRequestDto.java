package com.example.baro.domain.promise.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class PromiseVoteRequestDto {
    LocalDate date;
    LocalTime timeStart;
    LocalTime timeEnd;
    Long placeId;
    String placeName;
}
