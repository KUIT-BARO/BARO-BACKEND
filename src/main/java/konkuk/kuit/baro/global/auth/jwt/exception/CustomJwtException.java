package konkuk.kuit.baro.global.auth.jwt.exception;

import io.jsonwebtoken.JwtException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.Getter;

@Getter
public class CustomJwtException extends JwtException {
    private final ErrorCode errorCode;

    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

