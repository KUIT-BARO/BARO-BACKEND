package konkuk.kuit.baro.global.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum SuccessCode implements ResponseStatus{

    SUCCESS(20000, HttpStatus.OK, "요청에 성공하였습니다."),
    PROMISE_SUGGEST_SUCCESS(20001, HttpStatus.OK, "약속 제안에 성공하였습니다.");

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
