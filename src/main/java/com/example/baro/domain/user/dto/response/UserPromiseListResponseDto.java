package com.example.baro.domain.user.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record UserPromiseListResponseDto(
		String name,
		List<PendingPromisesDto> pendingPromises,
		List<UpcomingPromiseDto> upcomingPromises
) {
	@Builder
	public record PendingPromisesDto(
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