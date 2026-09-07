package com.github.alxsshv.measurementbpmapplication.common.dto.employee.mapper;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeDtoMapperTest {

    private final EmployeeDtoMapper mapper = new EmployeeDtoMapper();

    private Employee createEmployee() {
        return new Employee("1", "Иван", "Иванов", "Иванович", "11111111111");
    }

    private EmployeeDto createEmployeeDto() {
        return new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111");
    }

    @Nested
    class TestToDtoMethod {

        @Test
        @DisplayName("Преобразует сущность в DTO со всеми полями")
        void mapsEntityToDto() {
            Employee employee = createEmployee();

            EmployeeDto dto = mapper.toDto(employee);

            assertEquals(employee.getId(), dto.id());
            assertEquals(employee.getName(), dto.name());
            assertEquals(employee.getSurname(), dto.surname());
            assertEquals(employee.getPatronymic(), dto.patronymic());
            assertEquals(employee.getSnils(), dto.snils());
        }

        @Test
        @DisplayName("Возвращает DTO с null id, если у сущности id равен null")
        void mapsEntityWithNullId() {
            Employee employee = new Employee(null, "Иван", "Иванов", "Иванович", "11111111111");

            EmployeeDto dto = mapper.toDto(employee);

            assertNull(dto.id());
            assertNotNull(dto.name());
        }
    }

    @Nested
    class TestToEntityMethod {

        @Test
        @DisplayName("Преобразует DTO в сущность со всеми полями")
        void mapsDtoToEntity() {
            EmployeeDto dto = createEmployeeDto();

            Employee employee = mapper.toEntity(dto);

            assertEquals(dto.id(), employee.getId());
            assertEquals(dto.name(), employee.getName());
            assertEquals(dto.surname(), employee.getSurname());
            assertEquals(dto.patronymic(), employee.getPatronymic());
            assertEquals(dto.snils(), employee.getSnils());
        }
    }

    @Nested
    class TestToDtoListMethod {

        @Test
        @DisplayName("Преобразует список сущностей в список DTO")
        void mapsEntityListToDtoList() {
            List<Employee> employees = List.of(createEmployee());

            List<EmployeeDto> dtos = mapper.toDtoList(employees);

            assertEquals(1, dtos.size());
            assertEquals(employees.get(0).getId(), dtos.get(0).id());
            assertEquals(employees.get(0).getSnils(), dtos.get(0).snils());
        }

        @Test
        @DisplayName("Возвращает пустой список для пустого входного списка")
        void mapsEmptyList() {
            List<EmployeeDto> dtos = mapper.toDtoList(List.of());

            assertEquals(0, dtos.size());
        }
    }
}