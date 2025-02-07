package com.example.baro.domain.promise.dto.response;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import com.example.baro.common.entity.Promise;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromiseSuggestResponseDto(
		Long promiseId,          // 약속 ID
		String name,             // 약속명
		PromisePurpose purpose,          // 약속 목적
		LocalDate dateStart,     // 시작일
		LocalDate dateEnd,       // 종료일
		String placeName,            // 장소
		int peopleNumber,     // 참여 인원 수
		String leaderName
) {
}