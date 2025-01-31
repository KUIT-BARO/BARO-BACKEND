package com.example.baro.domain.user.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.user.dto.response.FindUserListResponseDto;
import com.example.baro.domain.user.dto.response.HomeResponseDto;
import com.example.baro.domain.user.dto.response.UserPromiseListResponseDto;
import com.example.baro.domain.user.dto.response.UserPromiseResponseDto;
import com.example.baro.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/promises")
	public ApiResponseDto<UserPromiseListResponseDto> getPromisePageInfo(@LoginUser User user) {
		UserPromiseListResponseDto userPromiseResponseDto = userService.getPromisePageInfo(user);
		return ApiResponseDto.success(SuccessCode.USER_DETAIL_GET_SUCCESS, userPromiseResponseDto);
	}

	@GetMapping("/search")
	public ApiResponseDto<FindUserListResponseDto> searchUsers(@RequestParam String code, @LoginUser User user) {
		FindUserListResponseDto responseDto = userService.searchUsersByCode(user.getId(), code);
		return ApiResponseDto.success(SuccessCode.USER_SEARCH_SUCCESS, responseDto);
	}

}
