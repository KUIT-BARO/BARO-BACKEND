package com.example.baro.common.Enum.dayOfWeek;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserProfile {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7),;

    private final int code;

    UserProfile(int code) {
        this.code = code;
    }

    public static UserProfile fromCode(int code) {
        for (UserProfile profile : UserProfile.values()) {
            if (profile.getCode() == code) {
                return profile;
            }
        }
        throw new IllegalArgumentException("알 수 없는 code입니다." + code);
    }

    public static UserProfile fromString(String status) {
        return Arrays.stream(UserProfile.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
    }


}