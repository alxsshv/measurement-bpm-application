package com.github.alxsshv.measurementbpmapplication.reportscheduler.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Репозиторий для хранения в виде файла данных о задачах на создание отчета о поверке для ФСА */
@Slf4j
@Component
@RequiredArgsConstructor
public class FsaReportTaskFileRepository implements FsaReportTaskRepository {
    private static final String FSA_REPORT_TASKS_FILENAME = "fsa_report_tasks_table.json";

    /** Пути хранения файлов из application.yml */
    private final PathsConfig paths;

    /** Класс для сериализации и десериализации объектов*/
    private final ObjectMapper objectMapper;

    @Override
    public Optional<FsaReportTask> findById(String id) {
         return findAll().stream()
                 .filter(e -> e.getId().equals(id))
                 .findAny();
    }

    @Override
    public List<FsaReportTask> findAll() {
        return readTasks();
    }

    @Override
    public void delete(String id) {
       List<FsaReportTask> tasks = findAll().stream()
               .filter(e -> !e.getId().equals(id))
               .toList();
       writeTasks(tasks);
    }

    @Override
    public FsaReportTask save(FsaReportTask task) {
        List<FsaReportTask> tasks = new ArrayList<>(findAll());
        tasks.add(task);
        writeTasks(tasks);
        Optional<FsaReportTask> taskOpt = findById(task.getId());
        return taskOpt.orElseThrow(
                () -> new FsaReportTaskStorageException(String.format("Ошибка сохранения задачи создания отчета о поверки для ФСА %s", task.getTitle())));
    }

    @Override
    public void update(FsaReportTask task) {
        Optional<FsaReportTask> taskFromDbOpt = findById(task.getId());
        if (taskFromDbOpt.isEmpty()){
            String errorMessage = "Информация о задаче создания отчета о поверки для ФСА с id = " + task.getId() + "не найдена";
            log.error(errorMessage);
            throw new FsaReportTaskStorageException(errorMessage);
        }
        List<FsaReportTask> tasks = new ArrayList<>(findAll().stream().filter(e -> !e.getId().equals(task.getId())).toList());
        tasks.add(task);
        writeTasks(tasks);
    }

    @Override
    public void deleteAll() {
        Path storagePath = Path.of(paths.fsaReportTaskStoragePath(), FSA_REPORT_TASKS_FILENAME);
        try {
            String fsaReportTasksData = objectMapper.writeValueAsString(new ArrayList<>());
            Files.writeString(storagePath, fsaReportTasksData);
            log.debug("Данные задач создания отчета о поверки для ФСА полностью удалены из файла [ {} ]", storagePath);
        } catch (IOException ex) {
            throw new FsaReportTaskStorageException("Ошибка удаления данных задач создания отчета о поверки для ФСА");
        }
    }

    private void writeTasks(List<FsaReportTask> tasks) {
        Path storagePath = Path.of(paths.fsaReportTaskStoragePath(), FSA_REPORT_TASKS_FILENAME);
        try {
            String fsaReportTasksData = objectMapper.writeValueAsString(tasks);
            Files.writeString(storagePath, fsaReportTasksData);
            log.debug("Данные задач создания отчета о поверки для ФСА успешно записаны в файл [ {} ]", storagePath);
        } catch (IOException ex) {
            String errorMessage = "Ошибка создания файла для хранения задач создания отчета о поверки для ФСА";
            log.error("{} по пути [ {} ]: {}", errorMessage, storagePath, ex.getMessage());
            throw new FsaReportTaskStorageException(errorMessage);
        }
    }

    private List<FsaReportTask> readTasks() {
        Path storagePath = Path.of(paths.fsaReportTaskStoragePath(), FSA_REPORT_TASKS_FILENAME);
        try {
            createEmptyStorageFileIfNotExists();
            byte[] fsaReportTasksData = Files.readAllBytes(storagePath);
            if (fsaReportTasksData.length > 0) {
                return List.of(objectMapper.readValue(fsaReportTasksData, FsaReportTask[].class));
            } else {
                return new ArrayList<>();
            }
        } catch (IOException ex) {
            String errorMessage = "Ошибка чтения файла для хранения задач создания отчета о поверки для ФСА";
            log.error("{} из файла [ {} ]: {}", errorMessage, storagePath, ex.getMessage());
            throw new FsaReportTaskStorageException(errorMessage);
        }
    }

    private void createEmptyStorageFileIfNotExists() {
        Path storagePath = Path.of(paths.fsaReportTaskStoragePath(), FSA_REPORT_TASKS_FILENAME);
        Path storageDirectory = Path.of(paths.fsaReportTaskStoragePath());
        if (!Files.exists(storageDirectory)) {
            try {
                Files.createDirectories(storageDirectory);
            } catch (IOException ex) {
                String errorMessage = "Ошибка создания директории для хранения файла задач создания отчета о поверки для ФСА";
                log.error("{} : {}", errorMessage, storageDirectory);
                throw new FsaReportTaskStorageException(errorMessage);
            }
        }
        if (!Files.exists(storagePath)) {
            log.debug("Файл для хранения для хранения задач создания отчета о поверки для ФСА не найден по пути [ {} ]", storagePath);
            writeTasks(new ArrayList<>());
        }
    }



}
