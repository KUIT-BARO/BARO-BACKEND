package konkuk.kuit.baro.global.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
public enum ErrorCode implements ResponseStatus {

    // 공통
    ILLEGAL_ARGUMENT(100, BAD_REQUEST.value(), "잘못된 요청값입니다."),
    NOT_FOUND(101, HttpStatus.NOT_FOUND.value(), "존재하지 않는 API 입니다."),
    METHOD_NOT_ALLOWED(102, HttpStatus.METHOD_NOT_ALLOWED.value(), "유효하지 않은 Http 메서드입니다."),
    SERVER_ERROR(103, INTERNAL_SERVER_ERROR.value(), "서버에 오류가 발생했습니다."),
    UNAUTHORIZED(104,HttpStatus.UNAUTHORIZED.value(),"인증 자격이 없습니다."),
    FORBIDDEN(105,HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
    // 유저
    USER_NOT_FOUND(201, HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."),
    USER_NAME_LENGTH(202, BAD_REQUEST.value(), "이름은 12자 이하입니다."),
    USER_CURRENT_PASSWORD_WRONG(203, BAD_REQUEST.value(), "입력하신 비밀번호가 현재 비밀번호와 일치하지 않습니다."),
    USER_NEW_PASSWORD_NOT_MATCH(204, BAD_REQUEST.value(), "입력하신 새 비밀번호와 일치하지 않습니다."),
    USER_NEW_PASSWORD_SAME(205, BAD_REQUEST.value(), "입력하신 새 비밀번호가 현재 비밀번호와 일치합니다."),
    USER_PASSWORD_LENGTH(206, BAD_REQUEST.value(), "비밀번호는 8자 이상입니다."),
    USER_ALREADY_EXISTS(207, BAD_REQUEST.value(), "중복된 이메일의 사용자가 존재합니다."),
    // Schedule
    INVALID_SCHEDULE_NAME(300, BAD_REQUEST.value(), "일정명은 12자 이하로 입력해야 합니다."),
    SCHEDULE_CONFLICT(301,CONFLICT.value(),"겹치는 일정이 존재합니다."),
    INVALID_SCHEDULE_TIME(302, BAD_REQUEST.value(), "시작 시간과 종료 시간이 동일할 수 없습니다."),
    SCHEDULE_NOT_FOUND(303, HttpStatus.NOT_FOUND.value(), "존재하지 않는 일정입니다."),
    SCHEDULE_NOT_EXISTS(304, HttpStatus.NOT_FOUND.value(), "등록된 일정이 없습니다."),
    // Place
    INVALID_LOCATION(400, BAD_REQUEST.value(), "위도, 경도값이 올바르지 않습니다."),
    PLACE_NOT_FOUND(401, HttpStatus.NOT_FOUND.value(), "존재하지 않는 장소입니다."),
    //Promise
    PROMISE_NOT_FOUND(500, HttpStatus.NOT_FOUND.value(),"존재하지 않는 약속입니다."),
    // 인증, 인가
    SECURITY_UNAUTHORIZED(500,HttpStatus.UNAUTHORIZED.value(), "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(501, HttpStatus.UNAUTHORIZED.value(), "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(502, HttpStatus.UNAUTHORIZED.value(), "refresh token이 유효하지 않습니다."),
    SECURITY_INVALID_ACCESS_TOKEN(503, HttpStatus.UNAUTHORIZED.value(), "access token이 유효하지 않습니다."),
    SECURITY_ACCESS_DENIED(504, HttpStatus.UNAUTHORIZED.value(), "접근 권한이 없습니다."),
    REFRESH_TOKEN_REQUIRED(505, BAD_REQUEST.value(), "refresh token이 필요합니다."),
    LOGIN_FAILED(506, BAD_REQUEST.value(), "이메일 혹은 비밀번호가 올바르지 않습니다.");
     @Getter
    private final int code;
    private final int httpStatus;
    private final String message;

    @Override
    public int getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
