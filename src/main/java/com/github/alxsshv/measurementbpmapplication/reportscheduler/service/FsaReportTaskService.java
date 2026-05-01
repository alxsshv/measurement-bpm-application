package com.github.alxsshv.measurementbpmapplication.reportscheduler.service;

import com.github.alxsshv.measurementbpmapplication.common.config.AppConstants;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskNotFoundException;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskProcessingException;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.repository.FsaReportTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FsaReportTaskService {

    private final FileStorageApi fileStorageApi;

    private final FsaReportTaskRepository fsaReportTaskRepository;

    public FsaReportTask create(MultipartFile file) {
        FsaReportTask task = createTask(file);
        try {
            fileStorageApi.upload(new FileDto(task.getContentFilename(), file.getBytes(), FileType.TEMPORARY));
            fsaReportTaskRepository.save(task);
        } catch (IOException ex) {
            String errorMessage = "Ошибка регистрации задачи создания отчета о поверки для ФСА: Ошибка создания временного файла";
            log.error("{} : {}", errorMessage, ex.getMessage());
            throw new FsaReportTaskProcessingException(errorMessage);
        }
        return task;
    }

    public FsaReportTask update(FsaReportTask task) {
        fsaReportTaskRepository.update(task);
        return fsaReportTaskRepository.findById(task.getId())
                .orElseThrow(() -> new FsaReportTaskProcessingException("Ошибка обновления данных задачи создания отчета о поверки для ФСА id = [ %s ]", task.getId()));
    }

    public void delete(FsaReportTask task) {
        fileStorageApi.delete(task.getContentFilename(), FileType.TEMPORARY);
        fsaReportTaskRepository.delete(task.getId());
    }

    public void deleteById(String id) {
        FsaReportTask task = getById(id);
        fileStorageApi.delete(task.getContentFilename(), FileType.TEMPORARY);
        fsaReportTaskRepository.delete(task.getId());
    }

    public List<FsaReportTask> getAll() {
        return fsaReportTaskRepository.findAll();
    }

    public FsaReportTask getById(String id) {
        return fsaReportTaskRepository.findById(id)
                .orElseThrow(() -> new FsaReportTaskNotFoundException("Задача id = [ %s ] создания отчета о поверке для ФСА не найдена", id));
    }

    private FsaReportTask createTask(MultipartFile file) {
        String id = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now(AppConstants.TIME_ZONE_ID);
        return FsaReportTask.builder()
                .id(UUID.randomUUID().toString())
                .title("Отчет по файлу " + file.getOriginalFilename())
                .contentFilename(String.format("%s_%s.tmp",id, timestamp))
                .status(FsaReportTask.Status.CREATED)
                .creationTimestamp(timestamp)
                .build();
    }

}

