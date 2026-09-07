package com.github.alxsshv.measurementbpmapplication.reports.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("java:S5976")
class DateStringConverterTest {

    @Nested
    class TestGetISOStringOrNullMethod {

        @Test
        @DisplayName("Преобразует LocalDateTime в ISO строку")
        void convertsDateTimeToIsoString() {
            LocalDateTime dateTime = LocalDateTime.of(2024, Month.JANUARY, 12, 14, 30, 25);

            String result = DateStringConverter.getISOStringOrNull(dateTime);

            assertEquals("2024-01-12T14:30:25", result);
        }

        @Test
        @DisplayName("Возвращает null, когда передаётся null")
        void returnsNullForNullInput() {
            assertNull(DateStringConverter.getISOStringOrNull(null));
        }
    }

    @Nested
    class TestGetStringOrNullMethod {

        @Test
        @DisplayName("Преобразует LocalDate в ISO строку")
        void convertsDateToString() {
            LocalDate date = LocalDate.of(2024, Month.JANUARY, 12);

            String result = DateStringConverter.getStringOrNull(date);

            assertEquals("2024-01-12", result);
        }

    }

    @Nested
    class TestParseLocalDateTimeOrGetNullMethod {

        @Test
        @DisplayName("Парсит валидную ISO строку в LocalDateTime")
        void parsesValidDateTimeString() {
            LocalDateTime result = DateStringConverter.parseLocalDateTimeOrGetNull("2024-01-12T14:30:25");

            assertEquals(LocalDateTime.of(2024, Month.JANUARY, 12, 14, 30, 25), result);
        }


        @Test
        @DisplayName("Бросает исключение для невалидной строки")
        void throwsForInvalidString() {
            assertThrows(DateTimeParseException.class,
                    () -> DateStringConverter.parseLocalDateTimeOrGetNull("invalid-date"));
        }
    }

    @Nested
    class TestParseLocalDateOrGetNullMethod {

        @Test
        @DisplayName("Парсит строку в формате yyyy-MM-dd")
        void parsesIsoDateString() {
            LocalDate result = DateStringConverter.parseLocalDateOrGetNull("2024-01-12");

            assertEquals(LocalDate.of(2024, Month.JANUARY, 12), result);
        }

        @Test
        @DisplayName("Парсит строку с датой и временем (извлекает первые 10 символов)")
        void parsesStringWithDateTimePrefix() {
            LocalDate result = DateStringConverter.parseLocalDateOrGetNull("2024-01-12T14:30:25");

            assertEquals(LocalDate.of(2024, Month.JANUARY, 12), result);
        }

        @Test
        @DisplayName("Парсит строку в формате dd.MM.yyyy")
        void parsesRussianDateFormat() {
            LocalDate result = DateStringConverter.parseLocalDateOrGetNull("12.01.2024");

            assertEquals(LocalDate.of(2024, Month.JANUARY, 12), result);
        }

        @Test
        @DisplayName("Возвращает null, когда передаётся null")
        void returnsNullForNullInput() {
            assertNull(DateStringConverter.parseLocalDateOrGetNull(null));
        }

        @Test
        @DisplayName("Бросает исключение для невалидной строки")
        void throwsForInvalidString() {
            assertThrows(DateTimeParseException.class,
                    () -> DateStringConverter.parseLocalDateOrGetNull("not-a-date"));
        }
    }
}