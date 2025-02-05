package com.example.baro.common.Enum.promisePurpose;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PromisePurpose {
    MEETING(1),
    STUDY(2),
    MEAL(3),
    CAFE(4);

    private final int code;

    PromisePurpose(int code) {
        this.code = code;
    }

    public static PromisePurpose fromCode(int code) {
        for (PromisePurpose purpose : PromisePurpose.values()) {
            if (purpose.getCode() == code) {
                return purpose;
            }
        }
        throw new IllegalArgumentException("알 수 없는 code입니다." + code);
    }

    public static PromisePurpose fromString(String status) {
        return Arrays.stream(PromisePurpose.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
    }


}