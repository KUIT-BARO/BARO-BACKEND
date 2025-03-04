package konkuk.kuit.baro.global.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode implements ResponseStatus {

    // 400번대
    ILLEGAL_ARGUMENT(40000, HttpStatus.BAD_REQUEST, "잘못된 요청값입니다."),
    NOT_FOUND(40400, HttpStatus.NOT_FOUND, "존재하지 않는 API 입니다."),
    USER_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

    // 500 번대
    SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");

    private final int code;
    @Getter
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
