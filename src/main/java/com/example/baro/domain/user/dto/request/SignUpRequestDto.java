package com.example.baro.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpRequestDto{
		String id;
		String password;
		String nickname;
}