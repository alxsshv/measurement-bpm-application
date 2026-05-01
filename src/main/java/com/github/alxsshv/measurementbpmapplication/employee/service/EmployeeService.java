package com.github.alxsshv.measurementbpmapplication.employee.service;

import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeAlreadyExistsException;
import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeNotFoundException;
import com.github.alxsshv.measurementbpmapplication.employee.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee create(@Valid Employee employee) {
        employee.setId(UUID.randomUUID().toString());
        Optional<Employee> employeeOpt = employeeRepository.findBySnils(employee.getSnils());
        if (employeeOpt.isPresent()) {
            String errorMessage = "Поверитель с указанным СНИЛС уже существует";
            log.error(errorMessage);
            throw new EmployeeAlreadyExistsException(errorMessage);
        }
        return employeeRepository.save(employee);
    }


    public void update(@Valid Employee employee) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());
        if (employeeOpt.isEmpty()){
            String errorMessage = " Информация о поверителе " + employee.getSurname()+ " " + employee.getName() +  " не найдена";
            log.error(errorMessage);
            throw new EmployeeNotFoundException(errorMessage);
        }
        Employee employeeFromDb = employeeOpt.get();
        employeeFromDb.updateFrom(employee);
        employeeRepository.update(employeeFromDb);
    }


    public void delete(String id) {
        employeeRepository.delete(id);
    }

    public Employee findById(String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        return employeeOpt.orElseThrow(
                () -> new EmployeeNotFoundException("Информация о поверителе с id = [ %s ] не найдена", id));
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

}
