package com.github.alxsshv.measurementbpmapplication.employee.service;

import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeAlreadyExistsException;
import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeNotFoundException;
import com.github.alxsshv.measurementbpmapplication.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee createEmployee(String id, String name, String surname, String patronymic, String snils) {
        return new Employee(id, name, surname, patronymic, snils);
    }

    @Nested
    class TestCreateMethod {

        @Test
        @DisplayName("Создаёт сотрудника с сгенерированным UUID id")
        void create_generatesUuidAndSaves() {
            Employee employee = createEmployee(null, "Иван", "Иванов", "Иванович", "11111111111");
            when(employeeRepository.findBySnils(employee.getSnils())).thenReturn(Optional.empty());
            when(employeeRepository.save(employee)).thenReturn(employee);

            Employee result = employeeService.create(employee);

            assertNotNull(result.getId());
            assertFalse(result.getId().isBlank());
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Создаёт сотрудника и сохраняет его через репозиторий")
        void create_savesViaRepository() {
            Employee employee = createEmployee(null, "Иван", "Иванов", "Иванович", "11111111111");
            when(employeeRepository.findBySnils(employee.getSnils())).thenReturn(Optional.empty());
            when(employeeRepository.save(employee)).thenReturn(employee);

            Employee result = employeeService.create(employee);

            assertSame(employee, result);
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Бросает исключение, если сотрудник с таким СНИЛС уже существует")
        void create_throwsWhenSnilsExists() {
            Employee employee = createEmployee(null, "Иван", "Иванов", "Иванович", "11111111111");
            Employee existing = createEmployee("1", "Петр", "Петров", "Петрович", "11111111111");
            when(employeeRepository.findBySnils(employee.getSnils())).thenReturn(Optional.of(existing));

            assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.create(employee));
            verify(employeeRepository, never()).save(employee);
        }
    }

    @Nested
    class TestUpdateMethod {

        @Test
        @DisplayName("Обновляет существующего сотрудника")
        void update_mergesFieldsAndPersists() {
            Employee existing = createEmployee("1", "Иван", "Иванов", "Иванович", "11111111111");
            Employee updateData = createEmployee("1", "Иван", "Иванов", "Иванович", "22222222222");
            when(employeeRepository.findById("1")).thenReturn(Optional.of(existing));

            employeeService.update(updateData);

            assertEquals("22222222222", existing.getSnils());
            verify(employeeRepository).update(existing);
        }

        @Test
        @DisplayName("Бросает исключение, если сотрудник не найден")
        void update_throwsWhenNotFound() {
            Employee updateData = createEmployee("1", "Иван", "Иванов", "Иванович", "22222222222");
            when(employeeRepository.findById("1")).thenReturn(Optional.empty());

            assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(updateData));
            verify(employeeRepository, never()).update(updateData);
        }
    }

    @Nested
    class TestDeleteMethod {

        @Test
        @DisplayName("Удаляет сотрудника по id через репозиторий")
        void delete_delegatesToRepository() {
            employeeService.delete("1");

            verify(employeeRepository).delete("1");
        }
    }

    @Nested
    class TestFindByIdMethod {

        @Test
        @DisplayName("Возвращает сотрудника, если он найден")
        void findById_returnsEmployeeWhenFound() {
            Employee employee = createEmployee("1", "Иван", "Иванов", "Иванович", "11111111111");
            when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));

            Employee result = employeeService.findById("1");

            assertSame(employee, result);
        }

        @Test
        @DisplayName("Бросает исключение, если сотрудник не найден")
        void findById_throwsWhenNotFound() {
            when(employeeRepository.findById("1")).thenReturn(Optional.empty());

            assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById("1"));
        }
    }

    @Nested
    class TestFindAllMethod {

        @Test
        @DisplayName("Возвращает всех сотрудников")
        void findAll_returnsList() {
            List<Employee> employees = List.of(
                    createEmployee("1", "Иван", "Иванов", "Иванович", "11111111111"),
                    createEmployee("2", "Петр", "Петров", "Петрович", "22222222222")
            );
            when(employeeRepository.findAll()).thenReturn(employees);

            List<Employee> result = employeeService.findAll();

            assertEquals(employees, result);
        }
    }
}
