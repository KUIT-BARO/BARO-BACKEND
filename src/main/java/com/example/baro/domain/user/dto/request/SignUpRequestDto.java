package com.example.baro.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequestDto{
		String id;
		String password;
		String nickname;
}