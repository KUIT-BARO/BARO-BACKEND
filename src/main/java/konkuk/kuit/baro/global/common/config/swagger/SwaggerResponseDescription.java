package konkuk.kuit.baro.global.common.config.swagger;

import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Getter
public enum SwaggerResponseDescription {
    USER_PROFILE_UPDATE(new LinkedHashSet<>(Set.of(
            USER_NAME_LENGTH
    )));
    private Set<ErrorCode> errorCodeList;
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

        if (this.name().contains("USER_")) {
            errorCodeList.add(USER_NOT_FOUND);
        }

        this.errorCodeList = errorCodeList;
    }
}
