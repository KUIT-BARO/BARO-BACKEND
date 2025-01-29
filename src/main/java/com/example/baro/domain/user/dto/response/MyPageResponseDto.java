package com.example.baro.domain.user.dto.response;

import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record MyPageResponseDto(
		String nickname,
		String code,
		List<ScheduleDto> schedule
) {
	@Builder
	public record ScheduleDto(
			String type, // "user" or "promise"
			Long id,
			String day, // e.g., "Monday", "Tuesday"
			String name, // e.g., "수업1", "수업2"
			LocalTime timeStart, // e.g., "09:00:00"
			LocalTime timeEnd // e.g., "11:00:00"
	) {
	}
}