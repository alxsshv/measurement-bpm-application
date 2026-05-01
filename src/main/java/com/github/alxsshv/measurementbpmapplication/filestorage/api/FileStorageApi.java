package com.github.alxsshv.measurementbpmapplication.filestorage.api;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.service.FileService;
import com.github.alxsshv.measurementbpmapplication.filestorage.service.FilenameGenerationService;
import com.github.alxsshv.measurementbpmapplication.filestorage.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileStorageApi {

    private final UploadService uploadService;
    private final FilenameGenerationService filenameGenerationService;

    private final FileService fileService;

    public void upload(FileDto fileDto) {
        uploadService.upload(fileDto);
        log.info("Файл [ {} ] успешно сохранен в файловое хранилище", fileDto.filename());
    }

    public Resource download(String filename, FileType fileType) {
        Resource fileResource = fileService.getFile(filename, fileType);
        log.info("Файл передан [ {} ]", filename);
        return fileResource;
    }

    public String generateFilename(FileType fileType) {
        String filename = filenameGenerationService.generateFilename(fileType);
        log.info("Сгенерировано имя файла: [ {} ]", filename);
        return filename;
    }

    public void delete(String filename, FileType fileType) {
        fileService.delete(filename, fileType);
        log.info("Файл [ {} ] удален", filename);
    }



}
