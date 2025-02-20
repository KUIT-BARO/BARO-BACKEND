package com.example.baro.domain.promise.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PromisePersonalPlaceRequestDto(
        @NotNull(message = "placeIds는 필수 입력값입니다.")
        @Size(min = 1, message = "최소 하나 이상의 장소를 입력해야 합니다.")
        List<Long> placeIds
) {
}