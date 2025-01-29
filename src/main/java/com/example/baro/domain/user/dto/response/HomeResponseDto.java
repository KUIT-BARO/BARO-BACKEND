package com.example.baro.domain.user.dto.response;

import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record HomeResponseDto(
		String name,
		UpcomingDdayDto upcomingDday,
		List<UpcomingScheduleDto> upcomingSchedules,
		List<ParticipantDto> participants
) {
	@Builder
	public record UpcomingDdayDto(
			Long promiseId,
			String name,
			String date,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place
	) {
	}

	@Builder
	public record UpcomingScheduleDto(
			Long promiseId,
			String name,
			String date,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place
	) {
	}

	@Builder
	public record ParticipantDto(
			Long userId,
			String nickname
	) {
	}
}