package com.github.alxsshv.measurementbpmapplication.filestorage.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.alxsshv.measurementbpmapplication.filestorage.utils.FileResponseEntityBuilder.buildFileResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogsController {
    private static final String LOGS_FILE_NAME = "spring.log";

    private final FileService fileService;

    /** Возвращает файл лога */
    @GetMapping(produces = "application/xml")
    public ResponseEntity<Resource> getLogs()  {
        Resource fileResource = fileService.getFile(LOGS_FILE_NAME, FileType.LOGS);
        return buildFileResponseEntity(fileResource, LOGS_FILE_NAME);
    }

}
