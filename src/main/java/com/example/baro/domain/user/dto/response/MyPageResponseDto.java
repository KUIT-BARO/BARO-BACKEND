package com.example.baro.domain.user.dto.response;
import com.example.baro.common.Enum.dayOfWeek.DayOfWeek;
import com.example.baro.common.Enum.userProfile.UserProfile;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record UserMyResponseDto(
		UserDto user,
		List<ScheduleDto> schedules,
		List<KeywordDto> keywords
) {
	@Builder
	public record UserDto(
			String nickname,
			String userId,
			UserProfile userProfile
	){
	}
	@Builder
	public record ScheduleDto(
			String name,
			DayOfWeek dayOfWeek,
			LocalTime timeStart,
			LocalTime timeEnd,
			String place
	){
	}
	@Builder
	public record  KeywordDto(
			Long keywordId,
			String keyword
	){}
	@Builder
	public record  ReviewDto(
			Long keywordId,
			String keyword
	){}

}