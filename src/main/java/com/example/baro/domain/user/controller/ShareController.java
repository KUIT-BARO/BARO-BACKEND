package com.example.baro.domain.user.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.user.dto.response.ShareLinkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class ShareController {

	private final String clientScheduleBaseUrl = "https://barobaro.netlify.app/schedule"; // 임시 클라 주소 (테스트용)
	private final String clientPromiseBaseUrl = "https://barobaro.netlify.app/suggest";  // 임시 클라 주소 (테스트용)
	private final String clientConfirmBaseUrl = "https://barobaro.netlify.app/confirm";

	@GetMapping("/schedule")
	public ApiResponseDto<ShareLinkResponseDto> getScheduleShareLink(@LoginUser User user) {
		final String url = clientScheduleBaseUrl + "/" + user.getId();
		ShareLinkResponseDto linkResponseDto = ShareLinkResponseDto.builder().url(url).build();
		return ApiResponseDto.success(SuccessCode.LINK_CREATE_SUCCESS, linkResponseDto);
	}

	@GetMapping("/promise/{promiseId}")
	public ApiResponseDto<ShareLinkResponseDto> getPromiseShareLink(@PathVariable Long promiseId) {
		final String url = clientPromiseBaseUrl + "/" + promiseId;
		ShareLinkResponseDto linkResponseDto = ShareLinkResponseDto.builder().url(url).build();
		return ApiResponseDto.success(SuccessCode.LINK_CREATE_SUCCESS, linkResponseDto);
	}

	@GetMapping("/confirm/{promiseId}")
	public ApiResponseDto<ShareLinkResponseDto> getConfirmPromiseShareLink(@PathVariable Long promiseId) {
		final String url = clientConfirmBaseUrl + "/" + promiseId;
		ShareLinkResponseDto linkResponseDto = ShareLinkResponseDto.builder().url(url).build();
		return ApiResponseDto.success(SuccessCode.LINK_CREATE_SUCCESS, linkResponseDto);
	}
}
