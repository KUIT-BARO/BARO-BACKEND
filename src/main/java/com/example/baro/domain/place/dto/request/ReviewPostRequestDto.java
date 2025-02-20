package com.example.baro.domain.place.dto.request;


import jakarta.validation.constraints.*;

import java.util.List;

public record ReviewPostRequestDto(
        @NotNull(message = "score는 필수 입력값입니다.")
        @DecimalMin(value = "1.0", message = "score는 최소 1.0 이상이어야 합니다.")
        @DecimalMax(value = "5.0", message = "score는 최대 5.0 이하여야 합니다.")
        Double score,

        @Size(max = 50, message = "note는 최대 50자까지 입력 가능합니다.")
        String note,
        @NotNull(message = "keywordIds는 필수 입력값입니다.")
        @Size(min = 1, message = "최소 하나 이상의 키워드를 선택해야 합니다.")
        List<@Positive(message = "keywordId는 1 이상의 숫자여야 합니다.") Long>keywordIds
) {}
