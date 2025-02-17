package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || !lt.isBefore(startTime)) &&
                (endTime == null || lt.isBefore(endTime));
    }

    public static boolean isWithinInclusive(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || !ld.isBefore(startDate)) &&
                (endDate == null || !ld.isAfter(endDate));
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

