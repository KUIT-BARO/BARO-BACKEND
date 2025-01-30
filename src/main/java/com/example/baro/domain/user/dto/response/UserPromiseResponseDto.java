package com.example.baro.domain.user.dto.response;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record UserPromiseResponseDto(
		List<UpcomingPromiseDto> upcomingPromises,
		NextPromiseDto nextPromise
) {
	@Builder
	public record UpcomingPromiseDto(
			Long promiseId,
			String name,
			LocalDate date,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place,
			String status
	) {
	}

	@Builder
	public record NextPromiseDto(
			Long promiseId,
			String name,
			LocalDate date,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place
	) {
	}
}