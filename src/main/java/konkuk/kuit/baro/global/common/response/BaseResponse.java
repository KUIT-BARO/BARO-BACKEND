package konkuk.kuit.baro.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import konkuk.kuit.baro.global.common.response.status.ResponseStatus;
import konkuk.kuit.baro.global.common.response.status.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static konkuk.kuit.baro.global.common.response.status.SuccessCode.*;

@Getter
@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    public BaseResponse(T data) {
        this.success = true;
        this.code = SUCCESS.getHttpStatus();   // 의논 사항
        this.message = SUCCESS.getMessage();   // 의논 사항
        this.data = data;
    }

    public BaseResponse(ResponseStatus status, T data) {
        this.success = true;
        this.code = status.getHttpStatus();
        this.message = status.getMessage();
        this.data = data;
    }

    public BaseResponse(int code, String message, T data) {
        this.success = true;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> of(int code, String message, T data) {
        return new BaseResponse<>(code, message, data);
    }

    public static <T> BaseResponse<T> ok(T data) {
        return of(1, "성공하였습니다.", data);
    }
}
