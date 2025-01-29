package com.example.baro.domain.user.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
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

	private final Long userId = 1L; //  임시 id (로그인 구현전)
	private final UserService userService;

	@GetMapping("/home")
	public ApiResponseDto<HomeResponseDto> getHomePageInfo() {
		HomeResponseDto homeResponseDto = userService.getHomePageInfo();
		return ApiResponseDto.success(SuccessCode.USER_DETAIL_GET_SUCCESS, homeResponseDto);
	}
}
