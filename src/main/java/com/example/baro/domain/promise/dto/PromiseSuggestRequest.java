package com.example.baro.domain.promise.dto;

import lombok.Getter;

@Getter
public class PromiseSuggestRequest {
    private String name;
    private String dateStart;
    private String dateEnd;
    private int peopleNum;
    private String purpose;
}
