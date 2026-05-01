package com.github.alxsshv.measurementbpmapplication.reports.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationReportDto;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.service.arshin.ArshinReportFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/arshin")
@RequiredArgsConstructor
public class ArshinController {

    private final ArshinReportFacade arshinReportFacade;

    @PostMapping()
    public ArshinVerificationReportDto parseFile(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            throw new ExcelFileException("Отсутствует файл для поиска данных о поверке");
        }
        return arshinReportFacade.buildReport(file);
    }
}
