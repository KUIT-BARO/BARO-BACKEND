package com.example.baro.domain.user.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record HomeResponseDto(
		String name,
		UpcomingDdayDto upcomingDday,
		List<UpcomingPromiseDto> upcomingPromises
) {
	@Builder
	public record UpcomingDdayDto(
			Long promiseId,
			String name,
			LocalDate date,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place,
			int peopleNumber
	) {
	}

	@Builder
	public record UpcomingPromiseDto(
			Long promiseId,
			String name,
			LocalDate date,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place,
			int peopleNumber
	) {
	}
}