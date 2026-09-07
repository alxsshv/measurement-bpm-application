package com.github.alxsshv.measurementbpmapplication.reportscheduler.controller;

import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.IllegalExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.TaskExceptionHandler;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.service.FsaReportTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReportsBuildingControllerTest {

    @Mock
    private FsaReportTaskService fsaReportTaskService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ReportsBuildingController(fsaReportTaskService))
                .setControllerAdvice(new TaskExceptionHandler())
                .build();
    }

    @Nested
    class TestBuildFsaReportMethod {

        @Test
        @DisplayName("Создаёт задачу по переданному файлу")
        void buildFsaReportCreatesTask() {
            ReportsBuildingController controller = new ReportsBuildingController(fsaReportTaskService);
            MockMultipartFile file = new MockMultipartFile("file", "book.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "content".getBytes());

            controller.buildFsaReport(file);

            verify(fsaReportTaskService).create(file);
        }

        @Test
        @DisplayName("Бросает исключение при передаче файла равного null")
        void buildFsaReportThrowsGivenNullFile() {
            ReportsBuildingController controller = new ReportsBuildingController(fsaReportTaskService);

            IllegalExcelFileException exception = assertThrows(IllegalExcelFileException.class,
                    () -> controller.buildFsaReport(null));

            assertEquals("Файл для формирования отчета не выбран или пуст", exception.getMessage());
        }
    }

    @Nested
    class TestGetTasksMethod {

        @Test
        @DisplayName("Возвращает список задач в виде DTO")
        void getTasksReturnsTaskDtos() throws Exception {
            FsaReportTask task = FsaReportTask.builder()
                    .id("1")
                    .title("Отчет по файлу book.xlsx")
                    .reportFilename("report.xml")
                    .status(FsaReportTask.Status.COMPLETED_SUCCESSFULLY)
                    .creationTimestamp(LocalDateTime.of(2024, Month.DECEMBER, 25, 14, 30))
                    .lastAttemptTimeStamp(LocalDateTime.of(2024, Month.DECEMBER, 26, 9, 5))
                    .build();
            when(fsaReportTaskService.getAll()).thenReturn(List.of(task));

            mockMvc.perform(get("/scheduled/report/fsa"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].title").value("Отчет по файлу book.xlsx"))
                    .andExpect(jsonPath("$[0].reportFilename").value("report.xml"))
                    .andExpect(jsonPath("$[0].status").value("ОТЧЕТ ГОТОВ"))
                    .andExpect(jsonPath("$[0].creationTimestamp").value("25.12.2024 14:30:00"))
                    .andExpect(jsonPath("$[0].lastAttemptTimeStamp").value("26.12.2024 09:05:00"));
        }

        @Test
        @DisplayName("Возвращает пустой список")
        void getTasksReturnsEmptyList() throws Exception {
            when(fsaReportTaskService.getAll()).thenReturn(List.of());

            mockMvc.perform(get("/scheduled/report/fsa"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    class TestDeleteTaskMethod {

        @Test
        @DisplayName("Удаляет задачу по идентификатору")
        void deleteTaskById() throws Exception {
            mockMvc.perform(delete("/scheduled/report/fsa/1"))
                    .andExpect(status().isNoContent());

            verify(fsaReportTaskService).deleteById("1");
        }
    }
}