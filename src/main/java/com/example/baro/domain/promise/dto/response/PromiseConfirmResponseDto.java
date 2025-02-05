package com.example.baro.domain.promise.dto.response;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromiseConfirmResponseDto (
        PromiseConfirmDto promiseConfirmDto
){

    @Builder
    public record PromiseConfirmDto (
            Long promiseId,
            String name,
            LocalDate date,
            int peopleNum,
            PromisePurpose purpose,
            String placeName,
            String UserName
    ){}
}

