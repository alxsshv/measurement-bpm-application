package com.github.alxsshv.measurementbpmapplication.filestorage.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileStorageExceptionHandlerTest {

    private final FileStorageExceptionHandler handler = new FileStorageExceptionHandler();

    @Nested
    class TestHandleFileOperationExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForFileOperationException() {
            ServiceMessage message = handler.handleFileOperationException(new FileOperationException("Ошибка сохранения файла"));

            assertEquals("Ошибка сохранения файла", message.message());
        }
    }

    @Nested
    class TestHandleFileNotFoundExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForFileNotFoundException() {
            ServiceMessage message = handler.handleFileNotFoundException(new FileNotFoundException("Файл не найден"));

            assertEquals("Файл не найден", message.message());
        }
    }

    @Nested
    class TestHandleConstraintViolationExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForConstraintViolationException() {
            ConstraintViolationException ex = mock(ConstraintViolationException.class);
            when(ex.getMessage()).thenReturn("Некорректное содержание файла");

            ServiceMessage message = handler.handleConstraintViolationException(ex);

            assertEquals("Некорректное содержание файла", message.message());
        }
    }
}