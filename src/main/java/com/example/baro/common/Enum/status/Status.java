package com.example.baro.common.Enum.status;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE(1),       // 1로 매핑 수락
    INACTIVE(2),     // 2로 매핑 거절
    SUSPENDED(3);    // 3로 매핑 응답대기

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public static Status fromCode(int code) {
        for (Status status : Status.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("알 수 없는 status code 입니다." + code);
    }
}
