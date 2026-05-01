package com.github.alxsshv.measurementbpmapplication.reports.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateStringConverter {
    public static String getISOStringOrNull(LocalDateTime dateTime){
        if (dateTime != null){
            return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        return null;
    }

    public static String getStringOrNull(LocalDate date){
        if (date != null){
            return date.toString();
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTimeOrGetNull(String dateTimeString){
        if (dateTimeString != null){
            return LocalDateTime.parse(dateTimeString);
        }
        return null;
    }

    public static LocalDate parseLocalDateOrGetNull(String dateString){
        if (dateString != null){
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
                return LocalDate.parse(dateString.substring(0, 10), formatter);
            } catch (DateTimeParseException ex) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneOffset.UTC);
                return LocalDate.parse(dateString.substring(0, 10), formatter);
            }
        }
        return null;
    }
}
