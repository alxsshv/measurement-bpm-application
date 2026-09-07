package com.github.alxsshv.measurementbpmapplication.reports.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaReportDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaVerificationRecordDto;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportsExceptionHandler;
import com.github.alxsshv.measurementbpmapplication.reports.service.fsa.FsaReportFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class FsaControllerTest {

    @Mock
    private FsaReportFacade fsaReportFacade;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FsaController(fsaReportFacade))
                .setControllerAdvice(new ReportsExceptionHandler())
                .build();
    }

    @Nested
    class TestParseFileMethod {

        @Test
        @DisplayName("Возвращает результат построения отчёта при валидном файле")
        void testParseFileReturnsReportDto() throws Exception {
            FsaVerificationRecordDto vrfRecord = new FsaVerificationRecordDto();
            vrfRecord.setNumberVerification("123456");
            vrfRecord.setResultVerification("годен");
            FsaReportDto report = new FsaReportDto("fsa-report.xml", List.of(vrfRecord));
            when(fsaReportFacade.buildReport(any(MultipartFile.class))).thenReturn(report);

            MockMultipartFile file = new MockMultipartFile("file", "book.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "content".getBytes());

            mockMvc.perform(multipart("/fsa").file(file))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.filename").value("fsa-report.xml"))
                    .andExpect(jsonPath("$.records.length()").value(1))
                    .andExpect(jsonPath("$.records[0].numberVerification").value("123456"))
                    .andExpect(jsonPath("$.records[0].resultVerification").value("годен"));

            ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
            verify(fsaReportFacade).buildReport(fileCaptor.capture());
            assertEquals("book.xlsx", fileCaptor.getValue().getOriginalFilename());
        }

        @Test
        @DisplayName("Возвращает 400 и сообщение об ошибке, когда фасад не смог построить отчёт")
        void testParseFileReturnsBadRequestWhenFacadeThrows() throws Exception {
            when(fsaReportFacade.buildReport(any(MultipartFile.class)))
                    .thenThrow(new ExcelFileException("Не удалось построить отчёт"));

            MockMultipartFile file = new MockMultipartFile("file", "book.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "content".getBytes());

            mockMvc.perform(multipart("/fsa").file(file))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Не удалось построить отчёт"));
        }

        @Test
        @DisplayName("Возвращает 400 при запросе без файла")
        void testParseFileReturnsBadRequestGivenEmptyMultipart() throws Exception {
            mockMvc.perform(multipart("/fsa"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Бросает исключение при передаче файла равного null")
        void testParseFileThrowsGivenNullFile() {
            FsaController controller = new FsaController(fsaReportFacade);

            ExcelFileException exception = assertThrows(ExcelFileException.class,
                    () -> controller.parseFile(null));

            assertEquals("Отсутствует файл для поиска данных о поверке", exception.getMessage());
        }
    }
}