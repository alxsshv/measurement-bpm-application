package com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.mapper;

import com.github.alxsshv.measurementbpmapplication.common.dto.reportscheduler.FsaReportTaskDto;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FsaReportTaskMapper {

    private FsaReportTaskMapper() {
    }

    public static FsaReportTaskDto toDto(FsaReportTask task) {
        String id = task.getId();
        String title = task.getTitle();
        String reportFilename = task.getReportFilename();
        String status = statusToString(task.getStatus());
        String creationTimestamp = getISOStringOrNull(task.getCreationTimestamp());
        String lastAttemptTimeStamp = getISOStringOrNull(task.getLastAttemptTimeStamp());
        return new FsaReportTaskDto(id, title, reportFilename, status, creationTimestamp, lastAttemptTimeStamp);
    }

    public static List<FsaReportTaskDto> toDtoList(List<FsaReportTask> tasks) {
        return tasks.stream().map(FsaReportTaskMapper::toDto).toList();
    }

    private static String statusToString(FsaReportTask.Status status) {
        return switch (status) {
            case CREATED -> "Задача зарегистрирована";
            case FAILED -> "Предыдущая попытка неудачна";
            case PROCESSED -> "Выполняется";
            case COMPLETED_SUCCESSFULLY -> "ОТЧЕТ ГОТОВ";
            case COMPLETED_UNSUCCESSFULLY -> "Создание отчета завершилось неудачей";
        };
    }

    private static String getISOStringOrNull(LocalDateTime dateTime){
        if (dateTime != null){
            return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        }
        return null;
    }




}
