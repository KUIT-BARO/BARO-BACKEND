package konkuk.kuit.baro.global.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import konkuk.kuit.baro.global.common.response.status.ResponseStatus;
import konkuk.kuit.baro.global.common.response.status.SuccessCode;
import lombok.Getter;

import static konkuk.kuit.baro.global.common.response.status.SuccessCode.*;

@Getter
@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseResponse<T> implements ResponseStatus {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    public BaseResponse(T data) {
        this.success = true;
        this.code = SUCCESS.getCode();         // 의논 사항
        this.message = SUCCESS.getMessage();   // 의논 사항
        this.data = data;
    }

    public BaseResponse(ResponseStatus status, T data) {
        this.success = true;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
