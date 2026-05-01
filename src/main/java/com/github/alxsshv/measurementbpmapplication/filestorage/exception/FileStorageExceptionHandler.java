package com.github.alxsshv.measurementbpmapplication.filestorage.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FileStorageExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceMessage handleFileOperationException(FileOperationException ex) {
        log.info("Ошибка обработки файла файловым хранилищем: {}", ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceMessage handleFileNotFoundException(FileNotFoundException ex) {
        log.info("Файл не найден: {}", ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceMessage handleConstraintViolationException(ConstraintViolationException ex) {
        log.info("Ошибка содержания файйла {}", ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

}
