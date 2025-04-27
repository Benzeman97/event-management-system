package com.event.api.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static LocalDateTime parseToLocalDateTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(time, formatter);
    }

    public static Instant parseToInstant(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(time, formatter).toInstant(ZoneOffset.UTC);
    }

    public static Instant convertLocalDateTimeToInstant(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }

    public static LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }
}
