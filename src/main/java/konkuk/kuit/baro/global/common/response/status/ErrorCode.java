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
    USER_NOT_FOUND(201, HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");

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
