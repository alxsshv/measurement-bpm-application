package com.github.alxsshv.measurementbpmapplication.employee.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeStorageException;
import com.github.alxsshv.measurementbpmapplication.employee.utils.PathResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Реа */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeFileStorageRepository implements EmployeeRepository {
    private static final String EMPLOYEE_STORAGE_FILENAME = "employee.json";

    /** Пути хранения файлов из application.yml */
    private final PathsConfig paths;

    /** Утилитарный класс для создания каталогов с хранением данных */
    private final PathResolver pathResolver;

    /** Класс для сериализации и десериализации объектов*/
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Employee> findById(String id) {
         return findAll().stream()
                 .filter(e -> e.getId().equals(id))
                 .findAny();
    }

    @Override
    public Optional<Employee> findBySnils(String snils) {
        return findAll().stream().filter(e -> e.getSnils().equals(snils)).findAny();
    }

    @Override
    public List<Employee> findAll() {
        return readEmployeeData();
    }

    @Override
    public void delete(String id) {
       List<Employee> employees = findAll().stream()
               .filter(e -> !e.getId().equals(id))
               .toList();
       writeEmployees(employees);
    }

    @Override
    public Employee save(Employee employee) {
        List<Employee> employees = new ArrayList<>(findAll());
        employees.add(employee);
        writeEmployees(employees);
        Optional<Employee> employeeOpt = findById(employee.getId());
        return employeeOpt.orElseThrow(
                () -> new EmployeeStorageException(String.format("Ошибка сохранения поверителя %s %s %s",
                        employee.getSurname(), employee.getName(), employee.getPatronymic())));
    }

    @Override
    public void update(Employee employee) {
        Optional<Employee> employeeFromDbOpt = findById(employee.getId());
        if (employeeFromDbOpt.isEmpty()){
            String errorMessage = "Информация о поверителе с id = " + employee.getId() + "не найдена";
            log.error(errorMessage);
            throw new EmployeeStorageException(errorMessage);
        }
        List<Employee> employees = new ArrayList<>(findAll().stream().filter(e -> !e.getId().equals(employee.getId())).toList());
        employees.add(employee);
        writeEmployees(employees);
    }

    @Override
    public void deleteAll() {
        Path storagePath = Path.of(paths.employeeStoragePath(), EMPLOYEE_STORAGE_FILENAME);
        try {
            String employeeData = objectMapper.writeValueAsString(new ArrayList<>());
            Files.writeString(storagePath, employeeData);
            log.debug("Данные поверителей полностью удалены из файла [ {} ]", storagePath);
        } catch (IOException ex) {
            throw new EmployeeStorageException("Ошибка удаления данных поверителей");
        }
    }

    private void writeEmployees(List<Employee> employees) {
        Path storagePath = Path.of(paths.employeeStoragePath(), EMPLOYEE_STORAGE_FILENAME);
        try {
            String employeeData = objectMapper.writeValueAsString(employees);
            Files.writeString(storagePath, employeeData);
            log.debug("Данные поверителей успешно записаны в файл [ {} ]", storagePath);
        } catch (IOException ex) {
            String errorMessage = "Ошибка создания файла для хранения сведений о поверителях";
            log.error("{} по пути [ {} ]: {}", errorMessage, storagePath, ex.getMessage());
            throw new EmployeeStorageException(errorMessage);
        }
    }

    private List<Employee> readEmployeeData() {
        try {
            pathResolver.createFilePathIfNotExist(paths.employeeStoragePath());
            createEmptyStorageFileIfNotExists();
            byte[] employeeData = Files.readAllBytes(Path.of(paths.employeeStoragePath(), EMPLOYEE_STORAGE_FILENAME));
            if (employeeData.length > 0) {
                return List.of(objectMapper.readValue(employeeData, Employee[].class));
            } else {
                return new ArrayList<>();
            }
        } catch (IOException ex) {
            String errorMessage = "Ошибка чтения файла сведений о поверителях";
            log.error("{} из файла [ {} ]: {}", errorMessage, paths.employeeStoragePath(), ex.getMessage());
            throw new EmployeeStorageException(errorMessage);
        }
    }

    private void createEmptyStorageFileIfNotExists() {
        Path storagePath = Path.of(paths.employeeStoragePath(), EMPLOYEE_STORAGE_FILENAME);
        if (!Files.exists(storagePath)) {
            log.debug("Файл для хранения сведений о поверителях не найден по пути [ {} ]", storagePath);
            writeEmployees(new ArrayList<>());
        }
    }



}
