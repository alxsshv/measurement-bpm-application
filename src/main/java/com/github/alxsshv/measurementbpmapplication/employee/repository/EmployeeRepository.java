package com.github.alxsshv.measurementbpmapplication.employee.repository;

import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Optional<Employee> findById(String id);
    Optional<Employee> findBySnils(String snils);
    List<Employee> findAll();
    void delete(String id);
    Employee save(Employee employee);
    void update(Employee employee);
    void deleteAll();
}
