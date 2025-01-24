package com.example.baro.domain.promise.dto;

import com.example.baro.domain.promise.entity.Promise;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PromiseSuggestResponse {
    private String name;
    // private String userName;
    // private String placeName;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private int peopleNum;
    private String purpose;

    public static PromiseSuggestResponse from(Promise promise) {
        return PromiseSuggestResponse.builder()
                .name(promise.getName())
                .dateStart(promise.getDate_start())
                .dateEnd(promise.getDate_end())
                .peopleNum(promise.getPeopleNum())
                .purpose(promise.getPurpose())
                .build();
    }
}
