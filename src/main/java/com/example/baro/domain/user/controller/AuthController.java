package com.example.baro.domain.user.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.exception.exceptionClass.DuplicateUserException;
import com.example.baro.common.exception.exceptionClass.InvalidRequestException;
import com.example.baro.domain.user.dto.request.LoginRequestDto;
import com.example.baro.domain.user.dto.request.SignUpRequestDto;
import com.example.baro.domain.user.dto.response.LoginResponseDto;
import com.example.baro.domain.user.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ApiResponseDto signup(@RequestBody SignUpRequestDto requestDto) {
		try {
			authService.signup(requestDto);
			return ApiResponseDto.success(SuccessCode.USER_SIGNUP_SUCCESS);
		} catch (DuplicateUserException e) {
			return ApiResponseDto.fail(ErrorCode.DUPLICATE_ENTITY_CONFLICT);
		} catch (InvalidRequestException e) {
			return ApiResponseDto.fail(ErrorCode.INVAILD_REQUEST_BODY);
		} catch (Exception e) {
			return ApiResponseDto.success(SuccessCode.USER_SIGNUP_SUCCESS);
		}
	}

	@PostMapping("/login")
	public ApiResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpSession session) {
		try {
			// 로그인 처리 (예: 아이디와 비밀번호 검증)
			User user = authService.login(requestDto);
			// 세션에 사용자 정보 저장
			session.setAttribute("user", user);

			return ApiResponseDto.success(SuccessCode.USER_LOGIN_SUCCESS, LoginResponseDto.builder().name(user.getNickname()).build());
		} catch (IllegalArgumentException e) {
			return ApiResponseDto.fail(ErrorCode.SECURITY_UNAUTHORIZED);
		} catch (DuplicateUserException e) {
			return ApiResponseDto.fail(ErrorCode.DUPLICATE_ENTITY_CONFLICT);
		} catch (Exception e) {
			return ApiResponseDto.fail(ErrorCode.SERVER_ERROR);
		}
	}

	@PostMapping("/logout")
	public ApiResponseDto logout(HttpSession session) {
		try {
			// 세션에서 사용자 정보 제거
			session.invalidate();  // 세션을 무효화하여 모든 정보 삭제
			return ApiResponseDto.success(SuccessCode.USER_LOGOUT_SUCCESS);
		} catch (Exception e) { 
			return ApiResponseDto.fail(ErrorCode.SERVER_ERROR);
		}
	}
}
