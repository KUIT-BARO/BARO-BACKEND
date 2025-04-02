package konkuk.kuit.baro.global.common.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class DateUtil {

    private DateUtil() {}

    public static int calculateDday(LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }
}

