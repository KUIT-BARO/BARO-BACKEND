package com.example.baro.domain.user.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
		String name
) {
}
