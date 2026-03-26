package konkuk.kuit.baro.global.auth.security.exception;

import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.Getter;

import javax.naming.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
