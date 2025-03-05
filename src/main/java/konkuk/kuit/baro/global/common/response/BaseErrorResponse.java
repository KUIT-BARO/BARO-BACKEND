package konkuk.kuit.baro.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import konkuk.kuit.baro.global.common.response.status.ResponseStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"success", "code", "message", "timestamp"})
public class BaseErrorResponse{
    private final boolean success;
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;

    public BaseErrorResponse(ResponseStatus status) {
        this.success = false;
        this.code = status.getHttpStatus();
        this.message = status.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public BaseErrorResponse(ResponseStatus status, String customMessage) {
        this.success = false;
        this.code = status.getHttpStatus();
        this.message = customMessage;
        this.timestamp = LocalDateTime.now();
    }
}
