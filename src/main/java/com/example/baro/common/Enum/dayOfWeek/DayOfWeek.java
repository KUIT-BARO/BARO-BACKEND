package com.example.baro.common.Enum.dayOfWeek;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DayOfWeek {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private final int code;

    DayOfWeek(int code) {
        this.code = code;
    }

    public static DayOfWeek fromCode(int code) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.getCode() == code) {
                return day;
            }
        }
        throw new IllegalArgumentException("알 수 없는 code입니다." + code);
    }

    public static DayOfWeek fromString(String status) {
        return Arrays.stream(DayOfWeek.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
    }

    // 🔹 커스텀 DayOfWeek → Java의 java.time.DayOfWeek 변환
    public java.time.DayOfWeek toJavaDayOfWeek() {
        return java.time.DayOfWeek.of(this.code); // Java의 DayOfWeek에서 직접 매핑
    }


}