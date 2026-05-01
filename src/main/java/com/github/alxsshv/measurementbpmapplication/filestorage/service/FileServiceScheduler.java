package com.github.alxsshv.measurementbpmapplication.filestorage.service;

import com.github.alxsshv.measurementbpmapplication.common.config.AppConstants;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.utils.FileStorgePathSelector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceScheduler {

    @Value("${storage.cleanup.filesTtlInHours}")
    private int defaultFileTtlInHours;

    private final FileStorgePathSelector fileStorgePathSelector;

    @Scheduled(fixedDelayString = "${storage.cleanup.intervalInMinutes.reportsFsa}", timeUnit = TimeUnit.MINUTES)
    @Async
    public void deleteOutdatedFsaReportFiles() throws IOException {
        log.info("Запущен процесс очитки устаревших отчетов о поверке для ФСА в файловом хранилище");
        Path workDir = Path.of(fileStorgePathSelector.getFileStoragePath(FileType.FSA_VERIFICATION_REPORT));
        try (Stream<Path> fileStream = Files.walk(workDir)) {
            fileStream
                    .filter(Files::isRegularFile)
                    .filter(path -> hasExtension(path, FileType.FSA_VERIFICATION_REPORT.getExtension()))
                    .filter(this::isOutdatedFile)
                    .forEach(this::deleteFile);
        } catch (NoSuchFileException ex) {
            log.error("Отсутствует директория для поиска файлов. Создаем директорию [ {} ]", workDir);
            Files.createDirectories(workDir);
        }
    }

    @Scheduled(fixedDelayString = "${storage.cleanup.intervalInMinutes.reportsArshin}", timeUnit = TimeUnit.MINUTES)
    @Async
    public void deleteOutdatedArshinReportFiles() throws IOException {
        log.info("Запущен процесс очитки устаревших отчетов о поверке для ФГИС Аршин в файловом хранилище");
        Path workDir = Path.of(fileStorgePathSelector.getFileStoragePath(FileType.ARSHIN_VERIFICATION_REPORT));
        try (Stream<Path> fileStream = Files.walk(workDir)) {
            fileStream
                    .filter(Files::isRegularFile)
                    .filter(path -> hasExtension(path, FileType.ARSHIN_VERIFICATION_REPORT.getExtension()))
                    .filter(this::isOutdatedFile)
                    .forEach(this::deleteFile);
        } catch (NoSuchFileException ex) {
            log.error("Отсутствует директория для поиска файлов. Создаем директорию [ {} ]", workDir);
            Files.createDirectories(workDir);
        }

    }

    @Scheduled(fixedDelayString = "${storage.cleanup.intervalInMinutes.temporary}", timeUnit = TimeUnit.MINUTES)
    @Async
    public void deleteOutdatedTemporaryFiles() throws IOException {
        log.info("Запущен процесс очитки временных файлов в файловом хранилище");
        Path workDir = Path.of(fileStorgePathSelector.getFileStoragePath(FileType.TEMPORARY));
        try (Stream<Path> fileStream = Files.walk(workDir)) {
            fileStream
                    .filter(Files::isRegularFile)
                    .filter(path -> hasExtension(path, FileType.TEMPORARY.getExtension()))
                    .filter(this::isOutdatedFile)
                    .forEach(this::deleteFile);
        } catch (NoSuchFileException ex) {
            log.error("Отсутствует директория для поиска файлов. Создаем директорию [ {} ]", workDir);
            Files.createDirectories(workDir);
        }

    }

    private boolean isOutdatedFile(Path filePath) {
        try {
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
            LocalDateTime creationTime = LocalDateTime.ofInstant(attr.creationTime().toInstant(), AppConstants.TIME_ZONE_ID);
            return creationTime.plusHours(defaultFileTtlInHours).isBefore(LocalDateTime.now(AppConstants.TIME_ZONE_ID));
        } catch (IOException ex) {
            log.error("Ошибка получения даты создания файла [ {} ]. Файл не подлежит удалению", filePath);
            return false;
        }
    }

    private boolean hasExtension(Path filePath, String extension) {
        int extensionStartIndex = filePath.toString().lastIndexOf(".");
        String fileExtension = filePath.toString().substring(extensionStartIndex);
        return fileExtension.equals(extension);
    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
            log.info("Очистка файлового хранилища. Файл [ {} ] успешно удален", path);
        } catch (IOException ex) {
            log.error("При попытке удаления устаревшего файла [ {} ] произошла ошибка: {}", path, ex.getMessage());
        }

    }

}
