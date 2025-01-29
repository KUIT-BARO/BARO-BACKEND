package com.example.baro.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FriendListResponseDto(
		List<FriendDto> friends
) {

	@Builder
	public record FriendDto(
			Long friendId,
			String code,
			String nickname,
			String profileImage
	) {
	}
}