package com.example.baro.common.exception.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    REFRESH_TOKEN_REQUIRED(BAD_REQUEST, "refresh token이 필요합니다."),
    PHOTO_NETWORK_ERROR(BAD_REQUEST, "스토리지에 사진을 저장하는 데 실패했습니다."),
    SOCIAL_INFO_NOT_EXTRACTED(BAD_REQUEST, "이메일을 추출할 수 없습니다."),
    ADMIN_PASSWORD_INCORRECT(BAD_REQUEST, "잘못된 비밀번호입니다."),

    // 401
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(UNAUTHORIZED, "refresh token이 유효하지 않습니다."),
    SECURITY_INVALID_ACCESS_TOKEN(UNAUTHORIZED, "access token이 유효하지 않습니다."),

    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "user을 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "review를 찾을 수 없습니다."),

    // 409
    DATE_FORMAT_CONFLICT(CONFLICT, "날짜 형식이 올바르지 않습니다."),

    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}