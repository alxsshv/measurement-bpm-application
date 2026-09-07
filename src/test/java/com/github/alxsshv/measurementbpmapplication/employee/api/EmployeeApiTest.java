package com.github.alxsshv.measurementbpmapplication.employee.api;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.employee.mapper.EmployeeDtoMapper;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeNotFoundException;
import com.github.alxsshv.measurementbpmapplication.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeApiTest {

    @Mock
    private EmployeeService employeeService;

    private final EmployeeDtoMapper mapper = new EmployeeDtoMapper();

    private EmployeeApi employeeApi;

    @BeforeEach
    void setUp() {
        employeeApi = new EmployeeApi(employeeService, mapper);
    }

    private Employee createEmployee(String id) {
        return new Employee(id, "Иван", "Иванов", "Иванович", "11111111111");
    }

    private EmployeeDto createEmployeeDto(String id) {
        return new EmployeeDto(id, "Иван", "Иванов", "Иванович", "11111111111");
    }

    @Nested
    class TestCreateMethod {

        @Test
        @DisplayName("Создаёт сотрудника из DTO и возвращает DTO результата")
        void createsEmployeeAndReturnsDto() {
            EmployeeDto dto = createEmployeeDto("1");
            Employee employeeFromService = mapper.toEntity(dto);
            when(employeeService.create(employeeFromService)).thenReturn(employeeFromService);

            EmployeeDto result = employeeApi.create(dto);

            assertEquals(dto, result);
            verify(employeeService).create(employeeFromService);
        }
    }

    @Nested
    class TestUpdateMethod {

        @Test
        @DisplayName("Преобразует DTO в сущность и обновляет сотрудника")
        void updatesEmployee() {
            EmployeeDto dto = createEmployeeDto("1");

            employeeApi.update(dto);

            verify(employeeService).update(mapper.toEntity(dto));
        }
    }

    @Nested
    class TestDeleteMethod {

        @Test
        @DisplayName("Удаляет сотрудника по переданному id")
        void deletesEmployee() {
            employeeApi.delete("1");

            verify(employeeService).delete("1");
        }
    }

    @Nested
    class TestFindByIdMethod {

        @Test
        @DisplayName("Возвращает DTO сотрудника, если он найден")
        void returnsDtoWhenEmployeeFound() {
            Employee employee = createEmployee("1");
            when(employeeService.findById("1")).thenReturn(employee);

            EmployeeDto result = employeeApi.findById("1");

            assertEquals(mapper.toDto(employee), result);
        }

        @Test
        @DisplayName("Пробрасывает исключение, если сотрудник не найден")
        void propagatesExceptionWhenEmployeeNotFound() {
            when(employeeService.findById("1")).thenThrow(new EmployeeNotFoundException("Not found"));

            assertThrows(EmployeeNotFoundException.class, () -> employeeApi.findById("1"));
        }
    }

    @Nested
    class TestFindAllMethod {

        @Test
        @DisplayName("Возвращает список DTO всех сотрудников")
        void returnsAllEmployeesAsDtos() {
            List<Employee> employees = List.of(createEmployee("1"));
            when(employeeService.findAll()).thenReturn(employees);

            List<EmployeeDto> result = employeeApi.findAll();

            assertEquals(mapper.toDtoList(employees), result);
        }
    }
}