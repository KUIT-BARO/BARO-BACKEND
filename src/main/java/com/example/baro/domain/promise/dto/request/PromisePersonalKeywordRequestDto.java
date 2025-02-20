package com.example.baro.domain.promise.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PromisePersonalKeywordRequestDto(
        @NotNull(message = "keywordIds는 필수 입력값입니다.")
        @Size(min = 1, message = "최소 하나 이상의 키워드를 입력해야 합니다.")
        List<Long> keywordIds
) {
}