package com.github.alxsshv.measurementbpmapplication.filestorage.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.github.alxsshv.measurementbpmapplication.filestorage.utils.FileResponseEntityBuilder.buildFileResponseEntity;

/** Контроллер для выгрузки отчетов в формате xml */
@RestController
@RequestMapping("/xml")
@RequiredArgsConstructor
public class XmlController {

    private final FileService fileService;


    /** Возвращает файл отчета для ФСА */
    @GetMapping(value = "/fsa", produces = "application/xml")
    public ResponseEntity<Resource> getXMLReportForFsa(@RequestParam("filename") String filename) {
        Resource fileResource = fileService.getFile(filename, FileType.FSA_VERIFICATION_REPORT);
        return buildFileResponseEntity(fileResource, filename);
    }

    /** Возвращает файл отчета для ФГИС Аршин */
    @GetMapping(value = "/arshin", produces = "application/xml")
    public ResponseEntity<Resource> getXMLReportForArshin(@RequestParam("filename") String filename)  {
        Resource fileResource = fileService.getFile(filename, FileType.ARSHIN_VERIFICATION_REPORT);
        return buildFileResponseEntity(fileResource, filename);
    }



}
