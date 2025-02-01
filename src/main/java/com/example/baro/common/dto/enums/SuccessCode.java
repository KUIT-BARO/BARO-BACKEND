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
	USER_LOGIN_SUCCESS(20001, HttpStatus.OK, "로그인에 성공하였습니다."),
	USER_LOGOUT_SUCCESS(20002, HttpStatus.OK, "로그아웃에 성공하였습니다."),
	USER_DETAIL_GET_SUCCESS(20003, HttpStatus.OK, "사용자 정보 조회 성공"),
	FRIEND_DETAIL_GET_SUCCESS(20004, HttpStatus.OK, "친구 정보 조회 성공"),
	SCHEDULE_GET_SUCCESS(20005, HttpStatus.OK, "시간표 조회 성공"),
	SCHEDULE_UPDATE_SUCCESS(20006, HttpStatus.OK, "시간표 수정 성공"),
	MY_SCHEDULE_GET_SUCCESS(20007, HttpStatus.OK, "나의 시간표 조회 성공"),


	//201 CREATED
	USER_SIGNUP_SUCCESS(20101, HttpStatus.CREATED, "회원가입에 성공하였습니다."),
	FRIEND_REQUEST_SUCCESS(20102, HttpStatus.CREATED, "친구 추가에 성공하였습니다."),
	MOVIE_BOOKING_POST_SUCCESS(20102, HttpStatus.CREATED, "영화 예매 성공"),
	SCHEDULE_REGISTER_SUCCESS(20103, HttpStatus.CREATED, "시간표 등록에 성공하였습니다."),

	//204 NO CONTENT
	FRIEND_DELETE_SUCCESS(200401, HttpStatus.OK, "친구 삭제 성공"),
	SCHEDULE_DELETE_SUCCESS(20402, HttpStatus.NO_CONTENT, "시간표 삭제 성공");


	private final int code;
	private final HttpStatus httpStatus;
	private final String message;
}