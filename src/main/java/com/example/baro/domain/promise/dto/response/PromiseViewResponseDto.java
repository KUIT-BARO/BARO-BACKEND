package com.example.baro.domain.promise.dto.response;

import com.example.baro.common.Enum.PromisePurpose.PromisePurpose;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromiseViewResponseDto(
        Long promiseId,          // 약속 ID
        String name,             // 약속명
        PromisePurpose purpose,          // 약속 목적
        LocalDate dateStart,     // 시작일
        LocalDate dateEnd,       // 종료일
        String place,            // 장소
        int peopleNumber         // 참여 인원 수
) {
}