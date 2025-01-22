package com.example.baro.domain.user.util.status;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE(1),       // 1로 매핑
    INACTIVE(2),     // 2로 매핑
    SUSPENDED(3);    // 3로 매핑

    private final int code;

    UserStatus(int code) {
        this.code = code;
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
