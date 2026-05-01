package com.github.alxsshv.measurementbpmapplication.filestorage.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.filestorage.exception.FileOperationException;
import com.github.alxsshv.measurementbpmapplication.filestorage.utils.FileStorgePathSelector;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Validated
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {

    private final FileStorgePathSelector pathSelector;

    public void upload(@Valid FileDto fileDto) {
        if (fileDto != null) {
            String storagePath = pathSelector.getFileStoragePath(fileDto.fileType());
            Path path = Path.of(storagePath, fileDto.filename());
            try {
                createDirectoryIfNotExists(storagePath);
                Files.write(path, fileDto.content());
            } catch (IOException ex) {
                throw new FileOperationException("Ошибка сохранения файла [ %s ] :%s", fileDto.filename(), ex.getMessage());
            }
        }
    }

    private void createDirectoryIfNotExists(String directory) throws IOException {
        Path directoryPath = Path.of(directory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException ex) {
                log.error("Ошибка создания директории [ {} ] :{}", directory, ex.getMessage());
                throw new IOException(ex);
            }
        }
    }
}
