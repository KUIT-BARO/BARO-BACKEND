package konkuk.kuit.baro.global.common.util;

import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class DateUtil {

    private DateUtil() {}

    public static int calculateDday(LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public static String getRemainingTimeUntilEndDate(LocalDateTime endDateTime) {
        LocalDateTime now = LocalDateTime.now(); // 현재 시간

        if (now.isAfter(endDateTime)) {
            throw new CustomException(ErrorCode.TIME_EXCEED);
        }

        long days = ChronoUnit.DAYS.between(now, endDateTime);
        if (days > 0) {
            return "D-" + days;
        }

        long hours = ChronoUnit.HOURS.between(now, endDateTime);
        long minutes = ChronoUnit.MINUTES.between(now, endDateTime) % 60;
        long seconds = ChronoUnit.SECONDS.between(now, endDateTime) % 60;

        StringBuilder time = new StringBuilder();
        if (hours > 0) {
            time.append(hours).append("시간 ");
        }

        if (minutes > 0) {
            time.append(minutes).append("분 ");
        }

        if (seconds > 0 || time.isEmpty()) {
            time.append(seconds).append("초");
        }

        return time.toString().trim();
    }

    // 날짜를 "yyyy년 M월 D일" 형식으로 변환
    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        return date.format(formatter);
    }

    public static String formatTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
}

