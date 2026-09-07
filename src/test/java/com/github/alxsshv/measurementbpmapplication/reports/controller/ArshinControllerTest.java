package com.github.alxsshv.measurementbpmapplication.reports.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationRecordDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationReportDto;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportsExceptionHandler;
import com.github.alxsshv.measurementbpmapplication.reports.service.arshin.ArshinReportFacade;
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
class ArshinControllerTest {

    @Mock
    private ArshinReportFacade arshinReportFacade;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ArshinController(arshinReportFacade))
                .setControllerAdvice(new ReportsExceptionHandler())
                .build();
    }

    @Nested
    class TestParseFileMethod {

        @Test
        @DisplayName("Возвращает результат построения отчёта при валидном файле")
        void testParseFileReturnsReportDto() throws Exception {
            ArshinVerificationReportDto report = new ArshinVerificationReportDto("arshin-report.xml",
                    List.of(ArshinVerificationRecordDto.builder()
                            .miTypeNumber("45201")
                            .serialNumber("12345")
                            .build()));
            when(arshinReportFacade.buildReport(any(MultipartFile.class))).thenReturn(report);

            MockMultipartFile file = new MockMultipartFile("file", "book.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "content".getBytes());

            mockMvc.perform(multipart("/arshin").file(file))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.filename").value("arshin-report.xml"))
                    .andExpect(jsonPath("$.records.length()").value(1))
                    .andExpect(jsonPath("$.records[0].miTypeNumber").value("45201"))
                    .andExpect(jsonPath("$.records[0].serialNumber").value("12345"));

            ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
            verify(arshinReportFacade).buildReport(fileCaptor.capture());
            assertEquals("book.xlsx", fileCaptor.getValue().getOriginalFilename());
        }

        @Test
        @DisplayName("Возвращает 400 и сообщение об ошибке, когда фасад не смог построить отчёт")
        void testParseFileReturnsBadRequestWhenFacadeThrows() throws Exception {
            when(arshinReportFacade.buildReport(any(MultipartFile.class)))
                    .thenThrow(new ExcelFileException("Не удалось построить отчёт"));

            MockMultipartFile file = new MockMultipartFile("file", "book.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "content".getBytes());

            mockMvc.perform(multipart("/arshin").file(file))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Не удалось построить отчёт"));
        }

        @Test
        @DisplayName("Возвращает 400 при запросе без файла")
        void testParseFileReturnsBadRequestGivenEmptyMultipart() throws Exception {
            mockMvc.perform(multipart("/arshin"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Бросает исключение при передаче файла равного null")
        void testParseFileThrowsGivenNullFile() {
            ArshinController controller = new ArshinController(arshinReportFacade);

            ExcelFileException exception = assertThrows(ExcelFileException.class,
                    () -> controller.parseFile(null));

            assertEquals("Отсутствует файл для поиска данных о поверке", exception.getMessage());
        }
    }
}