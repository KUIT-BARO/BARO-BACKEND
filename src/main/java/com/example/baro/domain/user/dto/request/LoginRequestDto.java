package com.example.baro.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto{
		String id;
		String password;
}