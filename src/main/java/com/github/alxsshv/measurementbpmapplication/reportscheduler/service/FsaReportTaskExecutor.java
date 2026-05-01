package com.github.alxsshv.measurementbpmapplication.reportscheduler.service;

import com.github.alxsshv.measurementbpmapplication.common.CustomMultipartFile;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaReportDto;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import com.github.alxsshv.measurementbpmapplication.reports.api.ReportsApi;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FsaReportTaskExecutor {
    private static final int INTERVAL_BETWEEN_ATTEMPTS_IN_MINUTES = 20;
    private static final int MAX_EXECUTION_TIME_IN_MINUTES = 900;

    private final FileStorageApi fileStorageApi;
    private final ReportsApi reportsApi;
    private final FsaReportTaskService fsaReportTaskService;

    @Async
    public void execute(FsaReportTask task) {
        log.info("Запущено выполнение задачи [ {} ] в потоке {}", task.getId(), Thread.currentThread().getName());
        if (isTaskComplete(task)) {
            log.info("Задача [ {} ] уже выполнена", task.getId());
            return;
        }
        if (isExceedMaxExecutionTime(task) && task.getStatus() != FsaReportTask.Status.COMPLETED_SUCCESSFULLY) {
            log.info("Срок выполнения задачи [ {} ] истек. Она помечена на удаление", task.getId());
            task.setStatus(FsaReportTask.Status.COMPLETED_UNSUCCESSFULLY);
            fsaReportTaskService.update(task);
        }
        if (isSuitableForExecution(task)) {
            log.info("Запущено формирование отчета по задаче [ {} ]", task.getId());
            executeTask(task);
        }
        log.info("Задача [ {} ] не выполняется т.к. не истек интервал между попытками", task.getId());
    }

    private boolean isSuitableForExecution(FsaReportTask task) {
        return !isTaskComplete(task) && isEnsureAttemptsTimeInterval(task);
    }

    private boolean isTaskComplete(FsaReportTask task) {
        return task.getStatus() == FsaReportTask.Status.COMPLETED_SUCCESSFULLY || task.getStatus() == FsaReportTask.Status.COMPLETED_UNSUCCESSFULLY;
    }

    private boolean isEnsureAttemptsTimeInterval(FsaReportTask task) {
        return task.getLastAttemptTimeStamp() == null ||
                task.getLastAttemptTimeStamp().plusMinutes(INTERVAL_BETWEEN_ATTEMPTS_IN_MINUTES).isBefore(LocalDateTime.now());
    }

    private boolean isExceedMaxExecutionTime(FsaReportTask task) {
        return task.getCreationTimestamp().plusMinutes(MAX_EXECUTION_TIME_IN_MINUTES).isBefore(LocalDateTime.now());
    }

    private void executeTask(FsaReportTask task) {
        log.info("Выполняется задача [ {} ]", task.getId());
        Resource resource = fileStorageApi.download(task.getContentFilename(), FileType.TEMPORARY);
        MultipartFile file = toMultipartFile(task, resource);
        task.setStatus(FsaReportTask.Status.PROCESSED);
        task.setLastAttemptTimeStamp(LocalDateTime.now());
        fsaReportTaskService.update(task);
        try {
            FsaReportDto fsaReportDto = reportsApi.createFsaReport(file);
            task.setStatus(FsaReportTask.Status.COMPLETED_SUCCESSFULLY);
            task.setReportFilename(fsaReportDto.getFilename());
        } catch (RuntimeException ex) {
            task.setStatus(FsaReportTask.Status.FAILED);
        }
        finally {
            fsaReportTaskService.update(task);
        }
    }

    private MultipartFile toMultipartFile(FsaReportTask task, Resource resource) {
        try {
            return CustomMultipartFile.builder()
                    .name(task.getContentFilename())
                    .originalFilename(task.getContentFilename())
                    .contentType("application/vnd.ms-excel")
                    .bytes(resource.getContentAsByteArray())
                    .build();
        } catch (IOException ex) {
            String errorMessage = "При запуске процесса создания отчета для ФСА возникла ошибка чтения данных из excel файла";
            log.error("{} [ {} ]: {}", errorMessage, task.getContentFilename(), ex.getMessage());
            throw new FsaReportTaskProcessingException(errorMessage);
        }
    }
}
