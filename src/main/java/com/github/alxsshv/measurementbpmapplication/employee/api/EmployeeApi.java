package com.github.alxsshv.measurementbpmapplication.employee.api;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.employee.mapper.EmployeeDtoMapper;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Component
@RequiredArgsConstructor
public class EmployeeApi {

    private final EmployeeService employeeService;
    private final EmployeeDtoMapper employeeDtoMapper;

    public EmployeeDto create(@Valid EmployeeDto employeeDto) {
       Employee employee = employeeDtoMapper.toEntity(employeeDto);
       Employee employeeFromService = employeeService.create(employee);
       return employeeDtoMapper.toDto(employeeFromService);
    }


    public void update(@Valid EmployeeDto employeeDto) {
        Employee employee = employeeDtoMapper.toEntity(employeeDto);
        employeeService.update(employee);
    }


    public void delete(String id) {
        employeeService.delete(id);
    }

    public EmployeeDto findById(String id) {
        Employee employeeFromService = employeeService.findById(id);
        return employeeDtoMapper.toDto(employeeFromService);
    }

    public List<EmployeeDto> findAll() {
        List<Employee> employees = employeeService.findAll();
        return employeeDtoMapper.toDtoList(employees);
    }

}
