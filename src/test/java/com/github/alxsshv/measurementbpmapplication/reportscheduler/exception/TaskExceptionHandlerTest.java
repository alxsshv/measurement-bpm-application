package com.github.alxsshv.measurementbpmapplication.reportscheduler.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskExceptionHandlerTest {

    private final TaskExceptionHandler handler = new TaskExceptionHandler();

    @Nested
    class TestHandleFsaReportTaskProcessingExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение при ошибке обработки задачи")
        void returnsMessageForProcessingException() {
            ServiceMessage message = handler.handleFsaReportTaskProcessingException(
                    new FsaReportTaskProcessingException("Ошибка обработки задачи"));

            assertEquals("Ошибка обработки задачи", message.message());
        }

        @Test
        @DisplayName("Возвращает сообщение при ошибке хранилища задач")
        void returnsMessageForStorageException() {
            ServiceMessage message = handler.handleFsaReportTaskProcessingException(
                    new FsaReportTaskStorageException("Ошибка хранилища задач"));

            assertEquals("Ошибка хранилища задач", message.message());
        }
    }

    @Nested
    class TestHandleFsaReportTaskNotFoundExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForNotFoundException() {
            ServiceMessage message = handler.handleFsaReportTaskNotFoundException(
                    new FsaReportTaskNotFoundException("Задача не найдена"));

            assertEquals("Задача не найдена", message.message());
        }
    }

    @Nested
    class TestHandleIllegalExcelFileExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForIllegalExcelFileException() {
            ServiceMessage message = handler.handleIllegalExcelFileException(
                    new IllegalExcelFileException("Файл не выбран"));

            assertEquals("Файл не выбран", message.message());
        }
    }
}