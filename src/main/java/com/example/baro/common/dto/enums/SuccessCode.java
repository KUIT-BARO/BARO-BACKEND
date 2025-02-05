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
	USER_PROFILE_IMAGE_CHANGE_SUCCESS(20005, HttpStatus.OK, "프로필 이미지 변경에 성공하셨습니다."),
	FRIEND_DELETE_SUCCESS(20006, HttpStatus.OK, "친구 삭제 성공"),
	USER_SEARCH_SUCCESS(20007, HttpStatus.OK, "사용자 검색 성공"),
	PROMISE_GET_SUCCESS(20008, HttpStatus.OK, "약속 정보 조회 성공"),
	UPCOMING_PROMISE_DELETE_SUCCESS(20010, HttpStatus.OK, "약속이 정상적으로 삭제되었습니다."),

	//201 CREATED
	USER_SIGNUP_SUCCESS(20101, HttpStatus.CREATED, "회원가입에 성공하였습니다."),
	FRIEND_REQUEST_SUCCESS(20102, HttpStatus.CREATED, "친구 추가에 성공하였습니다."),
	LINK_CREATE_SUCCESS(20103, HttpStatus.CREATED, "링크 생성 성공");



	private final int code;
	private final HttpStatus httpStatus;
	private final String message;
}