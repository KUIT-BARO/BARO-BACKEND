package com.example.baro.domain.user.dto.response;

import com.example.baro.common.Enum.userProfile.UserProfile;
import lombok.Builder;

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
			UserProfile profileImage
	) {
	}
}