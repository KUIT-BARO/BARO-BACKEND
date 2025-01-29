package com.example.baro.domain.promise.dto.request;

import lombok.Getter;

@Getter
public class PromiseSuggestRequestDto {
    private String name;
    private String dateStart;
    private String dateEnd;
    private int peopleNum;
    private String purpose;
}
