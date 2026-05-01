package com.github.alxsshv.measurementbpmapplication.reports.api;

import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationReportDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaReportDto;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.service.arshin.ArshinReportFacade;
import com.github.alxsshv.measurementbpmapplication.reports.service.fsa.FsaReportFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportsApi {

    private final ArshinReportFacade arshinReportFacade;

    private final FsaReportFacade fsaReportFacade;

    public ArshinVerificationReportDto createArshinReport(MultipartFile file)  {
        if (file == null) {
            throw new ExcelFileException("Отсутствует файл для поиска данных о поверке");
        }
        return arshinReportFacade.buildReport(file);
    }

    public FsaReportDto createFsaReport(MultipartFile file) {
        if (file == null) {
            throw new ExcelFileException("Отсутствует файл для поиска данных о поверке");
        }
        log.info("Создаем отчет о поверке");
        return fsaReportFacade.buildReport(file);

    }
}
