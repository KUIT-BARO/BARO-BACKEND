package com.example.baro.domain.user.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.user.dto.response.HomeResponseDto;
import com.example.baro.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/home")
	public ApiResponseDto<HomeResponseDto> getHomePageInfo(@LoginUser User user) {
		HomeResponseDto homeResponseDto = userService.getHomePageInfo(user);
		return ApiResponseDto.success(SuccessCode.USER_DETAIL_GET_SUCCESS, homeResponseDto);
	}
}
