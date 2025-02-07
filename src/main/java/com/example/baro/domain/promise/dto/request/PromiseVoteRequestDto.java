package com.example.baro.domain.promise.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class PromiseVoteRequestDto {
    Long promisePersonalTimeId;
    Long placeId;
}
