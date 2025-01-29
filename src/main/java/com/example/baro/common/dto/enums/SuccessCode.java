package com.example.baro.common.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

	/*
		SuccessCode 필요한거 등록해야됩니다.
	 */

	//200 OK
	USER_SIGNUP_SUCCESS(20002, HttpStatus.OK, "회원가입에 성공하였습니다."),
	USER_LOGIN_SUCCESS(20003, HttpStatus.OK, "로그인에 성공하였습니다."),
	USER_LOGOUT_SUCCESS(20004, HttpStatus.OK, "로그아웃에 성공하였습니다."),
	MOVIE_TIMELINE_GET_SUCCESS(20001, HttpStatus.OK, "영화 시간표 조회 성공"),
	USER_DETAIL_GET_SUCCESS(20002, HttpStatus.OK, "사용자 정보 조회 성공"),
	MOVIE_THEATER_GET_SUCCESS(20003, HttpStatus.OK, "영화관 조회 성공"),

	//201 CREATED
	MOVIE_BOOKING_POST_SUCCESS(20101, HttpStatus.CREATED, "영화 예매 성공");

	private final int code;
	private final HttpStatus httpStatus;
	private final String message;
}