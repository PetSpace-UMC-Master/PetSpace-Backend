package com.petspace.dev.util.formatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DayAfterFormatter {

    public static String formattingDayAfter(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(createdAt, now);
        long months = ChronoUnit.MONTHS.between(createdAt, now);
        long weeks = ChronoUnit.WEEKS.between(createdAt, now);
        long days = ChronoUnit.DAYS.between(createdAt, now);

        return calculateDayAfter(years, months, weeks, days);
    }

    private static String calculateDayAfter(long years, long months, long weeks, long days) {
        if (years != 0) {
            return years + "년 전";
        }

        if (months != 0) {
            return months + "달 전";
        }

        if (weeks != 0) {
            return weeks + "주 전";
        }

        if (days != 0) {
            return days + "일 전";
        }

        return "오늘";
    }
}
