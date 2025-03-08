package konkuk.kuit.baro.global.common.config.swagger;

import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Getter
public enum SwaggerResponseDescription {
    USER_PROFILE_UPDATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_NAME_LENGTH
    ))),
    USER_PROFILE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    USER_PASSWORD_UPDATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_CURRENT_PASSWORD_WRONG,
            USER_NEW_PASSWORD_NOT_MATCH,
            USER_NEW_PASSWORD_SAME
    ))),
    USER_DELETE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    )));
    private final Set<ErrorCode> errorCodeList;
    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        // 공통 에러
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
                ILLEGAL_ARGUMENT,
                NOT_FOUND,
                METHOD_NOT_ALLOWED,
                SERVER_ERROR,
                UNAUTHORIZED,
                FORBIDDEN
        )));


        this.errorCodeList = errorCodeList;
    }
}
