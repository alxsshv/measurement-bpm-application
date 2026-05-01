package com.github.alxsshv.measurementbpmapplication.filestorage.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.exception.FileNotFoundException;
import com.github.alxsshv.measurementbpmapplication.filestorage.exception.FileOperationException;
import com.github.alxsshv.measurementbpmapplication.filestorage.utils.FileStorgePathSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileStorgePathSelector pathSelector;

    public Resource getFile(String filename, FileType type) {
        String fileStoragePath = pathSelector.getFileStoragePath(type);
        try {
            return new ByteArrayResource(Files.readAllBytes(Path.of(fileStoragePath, filename)));
        } catch (IOException ex) {
            String errorMessage = "Файл не найден или поврежден. ";
            log.error("{}:{}", errorMessage, ex.getMessage());
            throw new FileNotFoundException(errorMessage);
        }
    }


    public void delete(String filename, FileType type) {
        Path filePath = Path.of(pathSelector.getFileStoragePath(type), filename);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileOperationException("Ошибка удаления файла [ %s ]: %s", filePath, ex.getMessage());
        }
    }







}





