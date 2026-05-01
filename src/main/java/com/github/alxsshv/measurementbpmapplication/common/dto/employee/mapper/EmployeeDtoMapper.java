package com.github.alxsshv.measurementbpmapplication.common.dto.employee.mapper;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeDtoMapper {

    public List<EmployeeDto> toDtoList(List<Employee> employees) {
        return employees.stream()
                .map(this::toDto)
                .toList();
    }

    public EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                employee.getPatronymic(),
                employee.getSnils()
        );
    }

    public Employee toEntity(EmployeeDto dto) {
        return new Employee(
                dto.id(),
                dto.name(),
                dto.surname(),
                dto.patronymic(),
                dto.snils()
        );
    }

}
