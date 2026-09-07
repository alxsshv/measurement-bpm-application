package com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.mapper;

import com.github.alxsshv.measurementbpmapplication.common.dto.reportscheduler.FsaReportTaskDto;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FsaReportTaskMapperTest {

    @Nested
    class TestToDtoMethod {

        @Test
        @DisplayName("Преобразует задачу в DTO со всеми полями")
        void toDtoMapsAllFields() {
            FsaReportTask task = createTask(FsaReportTask.Status.CREATED,
                    LocalDateTime.of(2024, Month.DECEMBER, 25, 14, 30),
                    LocalDateTime.of(2024, Month.DECEMBER, 26, 9, 5)
            );

            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(task);

            assertEquals("1", dto.id());
            assertEquals("Отчет по файлу book.xlsx", dto.title());
            assertEquals("report.xml", dto.reportFilename());
            assertEquals("Задача зарегистрирована", dto.status());
            assertEquals("25.12.2024 14:30:00", dto.creationTimestamp());
            assertEquals("26.12.2024 09:05:00", dto.lastAttemptTimeStamp());
        }

        @Test
        @DisplayName("Преобразует статус CREATED")
        void toDtoMapsCreatedStatus() {
            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(createTask(FsaReportTask.Status.CREATED, null, null));

            assertEquals("Задача зарегистрирована", dto.status());
        }

        @Test
        @DisplayName("Преобразует статус PROCESSED")
        void toDtoMapsProcessedStatus() {
            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(createTask(FsaReportTask.Status.PROCESSED, null, null));

            assertEquals("Выполняется", dto.status());
        }

        @Test
        @DisplayName("Преобразует статус FAILED")
        void toDtoMapsFailedStatus() {
            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(createTask(FsaReportTask.Status.FAILED, null, null));

            assertEquals("Предыдущая попытка неудачна", dto.status());
        }

        @Test
        @DisplayName("Преобразует статус COMPLETED_SUCCESSFULLY")
        void toDtoMapsCompletedSuccessfullyStatus() {
            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(createTask(FsaReportTask.Status.COMPLETED_SUCCESSFULLY, null, null));

            assertEquals("ОТЧЕТ ГОТОВ", dto.status());
        }

        @Test
        @DisplayName("Преобразует статус COMPLETED_UNSUCCESSFULLY")
        void toDtoMapsCompletedUnsuccessfullyStatus() {
            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(createTask(FsaReportTask.Status.COMPLETED_UNSUCCESSFULLY, null, null));

            assertEquals("Создание отчета завершилось неудачей", dto.status());
        }

        @Test
        @DisplayName("Возвращает null для пустых дат")
        void toDtoReturnsNullForEmptyDates() {
            FsaReportTaskDto dto = FsaReportTaskMapper.toDto(createTask(FsaReportTask.Status.FAILED, null, null));

            assertNull(dto.creationTimestamp());
            assertNull(dto.lastAttemptTimeStamp());
        }
    }

    @Nested
    class TestToDtoListMethod {

        @Test
        @DisplayName("Преобразует список задач")
        void toDtoListMapsAllTasks() {
            List<FsaReportTask> tasks = List.of(
                    createTask(FsaReportTask.Status.PROCESSED, null, null),
                    createTask(FsaReportTask.Status.COMPLETED_SUCCESSFULLY, null, null));

            List<FsaReportTaskDto> dtos = FsaReportTaskMapper.toDtoList(tasks);

            assertEquals(2, dtos.size());
            assertEquals("Выполняется", dtos.get(0).status());
            assertEquals("ОТЧЕТ ГОТОВ", dtos.get(1).status());
        }

        @Test
        @DisplayName("Возвращает пустой список для пустого входного списка")
        void toDtoListReturnsEmptyList() {
            assertTrue(FsaReportTaskMapper.toDtoList(List.of()).isEmpty());
        }
    }

    private FsaReportTask createTask(FsaReportTask.Status status, LocalDateTime creationTimestamp, LocalDateTime lastAttemptTimeStamp) {
        return FsaReportTask.builder()
                .id("1")
                .title("Отчет по файлу book.xlsx")
                .contentFilename("content.tmp")
                .reportFilename("report.xml")
                .status(status)
                .creationTimestamp(creationTimestamp)
                .lastAttemptTimeStamp(lastAttemptTimeStamp)
                .build();
    }
}