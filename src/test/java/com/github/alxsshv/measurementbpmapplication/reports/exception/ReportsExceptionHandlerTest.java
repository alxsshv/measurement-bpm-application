package com.github.alxsshv.measurementbpmapplication.reports.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.ExcelParseException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.IllegalEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportsExceptionHandlerTest {

    private final ReportsExceptionHandler handler = new ReportsExceptionHandler();

    @Nested
    class TestHandleIllegalEmployeeExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForIllegalEmployeeException() {
            ServiceMessage message = handler.handleIllegalEmployeeException(new IllegalEmployeeException("Сотрудник не найден"));

            assertEquals("Сотрудник не найден", message.message());
        }
    }

    @Nested
    class TestHandleExcelParseExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForExcelParseException() {
            ServiceMessage message = handler.handleExcelParseException(new ExcelParseException("Ошибка парсинга файла"));

            assertEquals("Ошибка парсинга файла", message.message());
        }
    }

    @Nested
    class TestHandleReportFileNotFoundExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForReportFileNotFoundException() {
            ServiceMessage message = handler.handleReportFileNotFoundException(new ReportFileNotFoundException("Отчет не найден"));

            assertEquals("Отчет не найден", message.message());
        }
    }

    @Nested
    class TestHandleArshinResponseExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForArshinResponseException() {
            ServiceMessage message = handler.handleArshinResponseException(new ArshinResponseException("Ошибка сервиса Аршин"));

            assertEquals("Ошибка сервиса Аршин", message.message());
        }
    }

    @Nested
    class TestHandleExcelFileExceptionMethod {

        @Test
        @DisplayName("Возвращает сообщение исключения")
        void returnsMessageForExcelFileException() {
            ServiceMessage message = handler.handleExcelFileException(new ExcelFileException("Отсутствует файл отчета"));

            assertEquals("Отсутствует файл отчета", message.message());
        }
    }
}