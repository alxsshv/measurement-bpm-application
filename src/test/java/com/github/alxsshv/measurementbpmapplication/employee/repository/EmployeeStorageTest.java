package com.github.alxsshv.measurementbpmapplication.employee.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.utils.PathResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class EmployeeStorageTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private EmployeeRepository storage;
    private final PathResolver pathResolver = new PathResolver();

    private PathsConfig paths;

    @TempDir
    private static Path tempDir;

    @BeforeEach
    void createStorageFile() throws IOException {
        paths = new PathsConfig(tempDir.toString(),tempDir.toString(),"",tempDir.toString(), tempDir.toString(), tempDir.toString());
        storage = new EmployeeFileStorageRepository(paths, pathResolver, objectMapper);
        Employee employee = new Employee();
        employee.setId("1");
        employee.setName("Иван");
        employee.setPatronymic("Иванов");
        employee.setSurname("Иванов");
        employee.setSnils("11111111111");
        Employee employee2 = new Employee();
        employee2.setId("2");
        employee2.setName("Петр");
        employee2.setPatronymic("Петрович");
        employee2.setSurname("Петров");
        employee2.setSnils("22222222222");
        String path = Path.of(paths.employeeStoragePath(), "employee.json").toString();
        FileWriter writer = new FileWriter(path, false);
        writer.write(objectMapper.writeValueAsString(List.of(employee, employee2)));
        writer.flush();
        writer.close();
    }

    @Test
    void testFindAll() {
        List<Employee> employees = storage.findAll();
        Assertions.assertEquals(2,employees.size());
    }

    @Test
    void testFindBySnils() {
        String snils = "11111111111";
        Optional<Employee> opt = storage.findBySnils(snils);
        Assertions.assertEquals(snils,opt.get().getSnils());
    }

    @Test
    void testFindById() {
        String id = "1";
        Optional<Employee> opt = storage.findById(id);
        Assertions.assertEquals(id,opt.get().getId());
    }

    @Test
    void testDelete() {
        String id = "2";
        storage.delete(id);
        Assertions.assertEquals(1, storage.findAll().size());
    }

    @Test
    void testSave() {
        Employee employee3 = new Employee();
        employee3.setId("3");
        employee3.setName("Сергей");
        employee3.setPatronymic("Сергеевич");
        employee3.setSurname("Сергеев");
        employee3.setSnils("333333333333");
        storage.save(employee3);
        Assertions.assertEquals(3,storage.findAll().size());
    }

    @Test
    void testUpdate() {
        String expectedSnils = "44444444444";
        String id = "1";
        Optional<Employee> employeeOpt = storage.findById(id);
        Employee employee = employeeOpt.get();
        employee.setSnils(expectedSnils);
        storage.update(employee);
        Assertions.assertEquals(expectedSnils,storage.findById(id).get().getSnils());
    }

    @Test
    void testDeleteAll() {
        storage.deleteAll();
        Assertions.assertTrue(storage.findAll().isEmpty());
    }


}
