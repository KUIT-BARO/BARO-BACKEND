package com.example.baro.domain.user.dto.response;

import com.example.baro.common.Enum.userProfile.UserProfile;
import lombok.Builder;
import java.util.List;

@Builder
public record FindUserListResponseDto(List<FindUserDto> users) {

	@Builder
	public record FindUserDto(
			Long userId,
			boolean isFriend,
			String code,
			String nickname,
			UserProfile profileImage
	) {
	}
}

