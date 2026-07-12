package com.github.alxsshv.measurementbpmapplication.employee.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.employee.mapper.EmployeeDtoMapper;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeDtoMapper employeeDtoMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDto> geEmployeeList() {
        List<Employee> employees = employeeService.findAll();
        return employeeDtoMapper.toDtoList(employees);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto getEmployee(@PathVariable("id") String id)  {
        Employee employee = employeeService.findById(id);
        return employeeDtoMapper.toDto(employee);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceMessage addEmployee(@RequestBody Employee employee)  {
        employeeService.create(employee);
        String okMessage = String.format("Поверитель %s %s успешно добавлен", employee.getName(), employee.getSurname());
        log.info(okMessage);
        return new ServiceMessage(okMessage);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceMessage editEmployee(@RequestBody Employee employee) {
        employeeService.update(employee);
        String okMessage = String.format("Сведения о поверителе %s %s обновлены", employee.getName(), employee.getSurname());
        log.info(okMessage);
        return new ServiceMessage(okMessage);
    }

    @DeleteMapping("{id}")
    public ServiceMessage deleteEmployee(@PathVariable("id") String id) {
        employeeService.delete(id);
        String okMessage ="Запись о поверителе успешно удалена";
        log.info(okMessage);
        return new ServiceMessage(okMessage);
    }
}

