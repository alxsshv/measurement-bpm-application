package com.github.alxsshv.measurementbpmapplication.employee.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeExceptionHandlerTest {

    private final EmployeeExceptionHandler handler = new EmployeeExceptionHandler();

    @Nested
    class TestHandleEmployeeAlreadyExistsExceptionMethod {

        @Test
        @DisplayName("Возвращает статус 400 и сообщение исключения")
        void returnsBadRequestForAlreadyExistsException() {
            ResponseEntity<ServiceMessage> response = handler.handleEmployeeAlreadyExistsException(
                    new EmployeeAlreadyExistsException("Сотрудник уже существует"));

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Сотрудник уже существует", response.getBody().message());
        }
    }

    @Nested
    class TestHandleEmployeeNotFoundExceptionMethod {

        @Test
        @DisplayName("Возвращает статус 404 и сообщение исключения")
        void returnsNotFoundForEmployeeNotFoundException() {
            ResponseEntity<ServiceMessage> response = handler.handleEmployeeNotFoundException(
                    new EmployeeNotFoundException("Сотрудник не найден"));

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Сотрудник не найден", response.getBody().message());
        }

        @Test
        @DisplayName("Возвращает статус 500 при ошибке хранилища сотрудников")
        void returnsInternalServerErrorForStorageException() {
            ResponseEntity<ServiceMessage> response = handler.handleEmployeeNotFoundException(
                    new EmployeeStorageException("Ошибка хранилища"));

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Ошибка хранилища", response.getBody().message());
        }
    }

    @Nested
    class TestCatchConstraintViolationExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение из второй части сообщения исключения")
        void returnsMessageFromSecondPartOfExceptionMessage() {
            ConstraintViolationException ex = mock(ConstraintViolationException.class);
            when(ex.getMessage()).thenReturn("Некорректные данные:строка 5");

            ResponseEntity<ServiceMessage> response = handler.catchConstraintViolationException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("строка 5", response.getBody().message());
        }

        @Test
        @DisplayName("Нумерует строку ошибки по отметке parse.excelObjects с однозначным номером")
        void returnsRowErrorMessageForSingleDigitRowNumber() {
            ConstraintViolationException ex = mock(ConstraintViolationException.class);
            when(ex.getMessage()).thenReturn("parse.excelObjects:Некорректные данные [7]");

            ResponseEntity<ServiceMessage> response = handler.catchConstraintViolationException(ex);

            assertEquals("Ошибка в файле! Строка № 9: Некорректные данные [7]", response.getBody().message());
        }

        @Test
        @DisplayName("Нумерует строку ошибки по отметке parse.excelObjects с многозначным номером")
        void returnsRowErrorMessageForMultiDigitRowNumber() {
            ConstraintViolationException ex = mock(ConstraintViolationException.class);
            when(ex.getMessage()).thenReturn("parse.excelObjects:Некорректные данные [77]");

            ResponseEntity<ServiceMessage> response = handler.catchConstraintViolationException(ex);

            assertEquals("Ошибка в файле! Строка № 79: Некорректные данные [77]", response.getBody().message());
        }
    }
}