package konkuk.kuit.baro.domain.schedule.model;

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

    public static DayOfWeek ofCode(Integer code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
