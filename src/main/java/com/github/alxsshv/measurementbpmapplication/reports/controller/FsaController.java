package com.github.alxsshv.measurementbpmapplication.reports.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaReportDto;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.service.fsa.FsaReportFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fsa")
@RequiredArgsConstructor
public class FsaController {

    private final FsaReportFacade fsaReportFacade;

    @PostMapping()
    public FsaReportDto parseFile(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            throw new ExcelFileException("Отсутствует файл для поиска данных о поверке");
        }
        return fsaReportFacade.buildReport(file);

    }
}
