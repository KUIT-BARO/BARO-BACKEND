package com.example.baro.domain.promise.dto.response;

import com.example.baro.common.Enum.status.Status;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record PromiseVoteResponseDto (

        PromiseVoteDto promiseVoteDto
){

    @Builder
    public record PromiseVoteDto (
       LocalDate date,
       LocalTime timeStart,
       LocalTime timeEnd,
       String placeName,
       Status status
    ){}
}

