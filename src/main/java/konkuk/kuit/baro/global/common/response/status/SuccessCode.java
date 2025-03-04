package konkuk.kuit.baro.global.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;


@AllArgsConstructor
public enum SuccessCode implements ResponseStatus{

    // 공통
    SUCCESS(100, OK.value(), "요청에 성공하였습니다."),

    // 약속
    PROMISE_SUGGEST_SUCCESS(200, OK.value(), "약속 제안에 성공하였습니다.");

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
