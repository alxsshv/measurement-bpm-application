package com.github.alxsshv.measurementbpmapplication.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CyrillicDateTimeFormatterTest {

    private LocalDateTime parse(String value) {
        DateTimeFormatter formatter = CyrillicDateTimeFormatter.getFormatter();
        return LocalDateTime.parse(value, formatter);
    }

    @Nested
    class TestGetFormatterMethod {

        @Test
        @DisplayName("Парсит дату и время без дня недели")
        void parsesDateTimeWithoutDayOfWeek() {
            LocalDateTime result = parse("12 Января 2024 14:30 +0300");

            assertEquals(LocalDateTime.of(2024, Month.JANUARY, 12, 14, 30), result);
        }

        @Test
        @DisplayName("Парсит дату и время с днём недели")
        void parsesDateTimeWithDayOfWeek() {
            LocalDateTime result = parse("Пн, 1 Января 2024 14:30 +0300");

            assertEquals(LocalDateTime.of(2024, Month.JANUARY, 1, 14, 30), result);
        }

        @Test
        @DisplayName("Парсит дату и время с секундами")
        void parsesDateTimeWithSeconds() {
            LocalDateTime result = parse("12 Января 2024 14:30:25 +0300");

            assertEquals(LocalDateTime.of(2024, Month.JANUARY, 12, 14, 30, 25), result);
        }

        @Test
        @DisplayName("Парсит дату и время со смещением часового пояса")
        void parsesDateTimeWithOffset() {
            LocalDateTime result = parse("12 Января 2024 14:30 +0500");

            assertEquals(LocalDateTime.of(2024, Month.JANUARY, 12, 14, 30), result);
        }

        @Test
        @DisplayName("Парсит строку в верхнем регистре (без учёта регистра)")
        void parsesUppercaseString() {
            LocalDateTime result = parse("пн, 1 ЯНВАРЯ 2024 14:30 +0300");

            assertEquals(LocalDateTime.of(2024, Month.JANUARY, 1, 14, 30), result);
        }

        @Test
        @DisplayName("Парсит однозначное число месяца")
        void parsesSingleDigitDayOfMonth() {
            LocalDateTime result = parse("2 Декабря 2024 09:05 +0300");

            assertEquals(LocalDateTime.of(2024, Month.DECEMBER, 2, 9, 5), result);
        }

        @Test
        @DisplayName("Бросает исключение при несуществующем названии месяца")
        void throwsOnUnknownMonth() {
            assertThrows(DateTimeException.class, () -> parse("12 Неизвестно 2024 14:30"));
        }

        @Test
        @DisplayName("Бросает исключение при отсутствии времени")
        void throwsWhenTimeIsMissing() {
            assertThrows(DateTimeException.class, () -> parse("12 Января 2024"));
        }

        @Test
        @DisplayName("Бросает исключение при однозначном часе")
        void throwsOnSingleDigitHour() {
            assertThrows(DateTimeException.class, () -> parse("12 Января 2024 9:30"));
        }
    }
}