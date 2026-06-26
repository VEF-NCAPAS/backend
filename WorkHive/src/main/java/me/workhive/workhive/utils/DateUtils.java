package me.workhive.workhive.utils;

import me.workhive.workhive.exceptions.BusinessRuleException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DateUtils {

    private DateUtils() {
    }

    public static void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BusinessRuleException(
                    "from date must be before or equal to to date"
            );
        }
    }

    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        return date.plusDays(1)
                .atStartOfDay()
                .minusNanos(1);
    }
}