package com.github.alxsshv.measurementbpmapplication.reportscheduler.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaReportDto;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import com.github.alxsshv.measurementbpmapplication.reports.api.ReportsApi;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FsaReportTaskExecutorTest {

    @Mock
    private FileStorageApi fileStorageApi;

    @Mock
    private ReportsApi reportsApi;

    @Mock
    private FsaReportTaskService fsaReportTaskService;

    private FsaReportTaskExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new FsaReportTaskExecutor(fileStorageApi, reportsApi, fsaReportTaskService);
    }

    @Nested
    class TestExecuteMethod {

        @Test
        @DisplayName("Не выполняет завершённую успешно задачу")
        void executeSkipsCompletedSuccessfullyTask() {
            FsaReportTask task = createTask(LocalDateTime.now(), null, FsaReportTask.Status.COMPLETED_SUCCESSFULLY);

            executor.execute(task);

            verify(fsaReportTaskService, never()).update(any(FsaReportTask.class));
        }

        @Test
        @DisplayName("Не выполняет завершённую неуспешно задачу")
        void executeSkipsCompletedUnsuccessfullyTask() {
            FsaReportTask task = createTask(LocalDateTime.now(), null, FsaReportTask.Status.COMPLETED_UNSUCCESSFULLY);

            executor.execute(task);

            verify(fsaReportTaskService, never()).update(any(FsaReportTask.class));
        }

        @Test
        @DisplayName("Помечает задачу как невыполненную при истечении срока")
        void executeMarksTaskUnsuccessfullyWhenMaxExecutionTimeExceeded() {
            FsaReportTask task = createTask(LocalDateTime.now().minusMinutes(1000), null, FsaReportTask.Status.CREATED);

            executor.execute(task);

            assertEquals(FsaReportTask.Status.COMPLETED_UNSUCCESSFULLY, task.getStatus());
            verify(fsaReportTaskService).update(task);
        }

        @Test
        @DisplayName("Выполняет задачу и помечает её как завершённую")
        void executeBuildsReportAndCompletesTask() {
            FsaReportTask task = createTask(LocalDateTime.now(), null, FsaReportTask.Status.CREATED);
            Resource resource = new ByteArrayResource("content".getBytes());
            when(fileStorageApi.download(anyString(), eq(FileType.TEMPORARY))).thenReturn(resource);
            when(reportsApi.createFsaReport(any(MultipartFile.class))).thenReturn(new FsaReportDto("report.xml", List.of()));

            executor.execute(task);

            assertEquals(FsaReportTask.Status.COMPLETED_SUCCESSFULLY, task.getStatus());
            assertEquals("report.xml", task.getReportFilename());
            verify(fsaReportTaskService, times(2)).update(task);
        }

        @Test
        @DisplayName("Помечает задачу как неудачную, когда не удалось построить отчёт")
        void executeMarksTaskFailedWhenReportApiThrows() {
            FsaReportTask task = createTask(LocalDateTime.now(), null, FsaReportTask.Status.CREATED);
            Resource resource = new ByteArrayResource("content".getBytes());
            when(fileStorageApi.download(anyString(), eq(FileType.TEMPORARY))).thenReturn(resource);
            when(reportsApi.createFsaReport(any(MultipartFile.class))).thenThrow(new RuntimeException("api error"));

            executor.execute(task);

            assertEquals(FsaReportTask.Status.FAILED, task.getStatus());
            verify(fsaReportTaskService, times(2)).update(task);
        }

        @Test
        @DisplayName("Не выполняет задачу, если интервал между попытками не истёк")
        void executeSkipsTaskWhenAttemptIntervalNotElapsed() {
            LocalDateTime now = LocalDateTime.now();
            FsaReportTask task = createTask(now.minusMinutes(10), now.minusMinutes(5), FsaReportTask.Status.CREATED);

            executor.execute(task);

            verify(fsaReportTaskService, never()).update(any(FsaReportTask.class));
        }

        @Test
        @DisplayName("Бросает исключение при ошибке чтения файла задачи")
        void executeThrowsProcessingExceptionOnResourceReadFailure() throws IOException {
            FsaReportTask task = createTask(LocalDateTime.now(), null, FsaReportTask.Status.CREATED);
            Resource resource = mock(Resource.class);
            when(resource.getContentAsByteArray()).thenThrow(new IOException("read failed"));
            when(fileStorageApi.download(anyString(), eq(FileType.TEMPORARY))).thenReturn(resource);

            assertThrows(FsaReportTaskProcessingException.class, () -> executor.execute(task));
        }
    }

    private FsaReportTask createTask(LocalDateTime creationTimestamp, LocalDateTime lastAttemptTimeStamp, FsaReportTask.Status status) {
        return FsaReportTask.builder()
                .id("1")
                .title("Отчет по файлу book.xlsx")
                .contentFilename("content.tmp")
                .status(status)
                .creationTimestamp(creationTimestamp)
                .lastAttemptTimeStamp(lastAttemptTimeStamp)
                .build();
    }
}