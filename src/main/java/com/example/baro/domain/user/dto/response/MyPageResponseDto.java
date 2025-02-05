package com.example.baro.domain.user.dto.response;
import com.example.baro.common.Enum.userProfile.UserProfile;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Builder
public record MyPageResponseDto(
		UserDto user,
		List<ScheduleDto> schedules,
		List<SavedPlaceDto> savedPlaces,
		List<MyReviewDto> myReviews
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
	public record  SavedPlaceDto(
			Long keywordId,
			String keyword
	){}
	@Builder
	public record  MyReviewDto(
			Long placeId,
			String name,
			String note,
			Integer score,
			BigDecimal latitude, // 위도
			BigDecimal longitude, // 경도
			List<String> Keywords
	){}

}