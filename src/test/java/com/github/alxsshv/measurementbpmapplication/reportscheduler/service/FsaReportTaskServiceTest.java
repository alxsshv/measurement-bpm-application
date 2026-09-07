package com.github.alxsshv.measurementbpmapplication.reportscheduler.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskNotFoundException;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskProcessingException;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.repository.FsaReportTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FsaReportTaskServiceTest {

    @Mock
    private FileStorageApi fileStorageApi;

    @Mock
    private FsaReportTaskRepository fsaReportTaskRepository;

    private FsaReportTaskService fsaReportTaskService;

    @BeforeEach
    void setUp() {
        fsaReportTaskService = new FsaReportTaskService(fileStorageApi, fsaReportTaskRepository);
    }

    @Nested
    class TestCreateMethod {

        @Test
        @DisplayName("Регистрирует задачу и сохраняет файл во временное хранилище")
        void createRegistersTaskAndUploadsFile() {
            when(fsaReportTaskRepository.save(any(FsaReportTask.class))).thenAnswer(invocation -> invocation.getArgument(0));
            MockMultipartFile file = new MockMultipartFile("file", "book.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "content".getBytes());

            FsaReportTask task = fsaReportTaskService.create(file);

            assertEquals(FsaReportTask.Status.CREATED, task.getStatus());
            assertNotNull(task.getId());
            assertNotNull(task.getCreationTimestamp());
            assertEquals("Отчет по файлу book.xlsx", task.getTitle());
            assertTrue(task.getContentFilename().endsWith(".tmp"));
            verify(fileStorageApi).upload(any(FileDto.class));
            verify(fsaReportTaskRepository).save(any(FsaReportTask.class));
        }

        @Test
        @DisplayName("Бросает исключение при ошибке чтения файла")
        void createThrowsProcessingExceptionOnReadFailure() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getOriginalFilename()).thenReturn("book.xlsx");
            when(file.getBytes()).thenThrow(new IOException("read failed"));

            assertThrows(FsaReportTaskProcessingException.class, () -> fsaReportTaskService.create(file));
        }
    }

    @Nested
    class TestUpdateMethod {

        @Test
        @DisplayName("Обновляет задачу и возвращает её")
        void updateReturnsUpdatedTask() {
            FsaReportTask task = createTask();
            when(fsaReportTaskRepository.findById(task.getId())).thenReturn(Optional.of(task));

            FsaReportTask result = fsaReportTaskService.update(task);

            assertSame(task, result);
            verify(fsaReportTaskRepository).update(task);
        }

        @Test
        @DisplayName("Бросает исключение при обновлении отсутствующей задачи")
        void updateThrowsProcessingExceptionForMissingTask() {
            FsaReportTask task = createTask();
            when(fsaReportTaskRepository.findById(task.getId())).thenReturn(Optional.empty());

            assertThrows(FsaReportTaskProcessingException.class, () -> fsaReportTaskService.update(task));
        }
    }

    @Nested
    class TestDeleteMethod {

        @Test
        @DisplayName("Удаляет задачу и связанный файл")
        void deleteRemovesTaskAndFile() {
            FsaReportTask task = createTask();

            fsaReportTaskService.delete(task);

            verify(fileStorageApi).delete(task.getContentFilename(), FileType.TEMPORARY);
            verify(fsaReportTaskRepository).delete(task.getId());
        }
    }

    @Nested
    class TestDeleteByIdMethod {

        @Test
        @DisplayName("Удаляет задачу по идентификатору")
        void deleteByIdRemovesTask() {
            FsaReportTask task = createTask();
            when(fsaReportTaskRepository.findById(task.getId())).thenReturn(Optional.of(task));

            fsaReportTaskService.deleteById(task.getId());

            verify(fileStorageApi).delete(task.getContentFilename(), FileType.TEMPORARY);
            verify(fsaReportTaskRepository).delete(task.getId());
        }

        @Test
        @DisplayName("Бросает исключение для отсутствующей задачи")
        void deleteByIdThrowsNotFoundExceptionForMissingTask() {
            when(fsaReportTaskRepository.findById("1")).thenReturn(Optional.empty());

            assertThrows(FsaReportTaskNotFoundException.class, () -> fsaReportTaskService.deleteById("1"));
        }
    }

    @Nested
    class TestGetAllMethod {

        @Test
        @DisplayName("Возвращает все задачи")
        void getAllReturnsTasks() {
            FsaReportTask task = createTask();
            when(fsaReportTaskRepository.findAll()).thenReturn(List.of(task));

            assertEquals(List.of(task), fsaReportTaskService.getAll());
        }
    }

    @Nested
    class TestGetByIdMethod {

        @Test
        @DisplayName("Возвращает задачу по идентификатору")
        void getByIdReturnsTask() {
            FsaReportTask task = createTask();
            when(fsaReportTaskRepository.findById(task.getId())).thenReturn(Optional.of(task));

            assertSame(task, fsaReportTaskService.getById(task.getId()));
        }

        @Test
        @DisplayName("Бросает исключение для отсутствующей задачи")
        void getByIdThrowsNotFoundExceptionForMissingTask() {
            when(fsaReportTaskRepository.findById("1")).thenReturn(Optional.empty());

            assertThrows(FsaReportTaskNotFoundException.class, () -> fsaReportTaskService.getById("1"));
        }
    }

    private FsaReportTask createTask() {
        return FsaReportTask.builder()
                .id("1")
                .title("Отчет по файлу book.xlsx")
                .contentFilename("content.tmp")
                .status(FsaReportTask.Status.CREATED)
                .creationTimestamp(LocalDateTime.of(2024, Month.DECEMBER, 25, 14, 30))
                .build();
    }
}