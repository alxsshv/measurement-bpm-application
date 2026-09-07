package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.employee.api.EmployeeApi;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.IllegalEmployeeException;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeFinderTest {

    @Mock
    private EmployeeApi employeeApi;

    private EmployeeFinder employeeFinder;

    @BeforeEach
    void setUp() {
        employeeFinder = new EmployeeFinder(employeeApi);
    }

    private EmployeeDto createEmployee(String surname, String name, String patronymic) {
        return new EmployeeDto("1", name, surname, patronymic, "11111111111");
    }

    @Nested
    class TestFindEmployeeMethod {

        @Test
        @DisplayName("Возвращает сотрудника по полному ФИО из списка")
        void findsEmployeeByFio() {
            EmployeeDto employee = createEmployee("Иванов", "Иван", "Иванович");
            when(employeeApi.findAll()).thenReturn(List.of(employee));

            EmployeeDto result = employeeFinder.findEmployee("Иванов Иван Иванович");

            assertEquals(employee, result);
            verify(employeeApi, times(1)).findAll();
        }

        @Test
        @DisplayName("Обновляет список сотрудников и находит сотрудника при первом промахе")
        void findsEmployeeAfterListRefresh() {
            EmployeeDto employee = createEmployee("Иванов", "Иван", "Иванович");
            when(employeeApi.findAll()).thenReturn(List.of(), List.of(employee));

            EmployeeDto result = employeeFinder.findEmployee("Иванов Иван Иванович");

            assertEquals(employee, result);
            verify(employeeApi, times(2)).findAll();
        }

        @Test
        @DisplayName("Бросает исключение, если ФИО содержит меньше трёх слов")
        void throwsWhenFioHasLessThanThreeWords() {
            assertThrows(IllegalEmployeeException.class,
                    () -> employeeFinder.findEmployee("Иванов Иван"));
            verify(employeeApi, never()).findAll();
        }

        @Test
        @DisplayName("Бросает исключение при передаче null в качестве ФИО")
        void throwsWhenFioIsNull() {
            assertThrows(NullPointerException.class, () -> employeeFinder.findEmployee(null));
        }

        @Test
        @DisplayName("Бросает исключение, если сотрудник не найден после обновления списка")
        void throwsWhenEmployeeNotFoundAfterRefresh() {
            when(employeeApi.findAll()).thenReturn(List.of());

            assertThrows(IllegalEmployeeException.class,
                    () -> employeeFinder.findEmployee("Петров Пётр Петрович"));
            verify(employeeApi, times(3)).findAll();
        }

        @Test
        @DisplayName("Не обращается к API, если сотрудник сразу найден в кэше")
        void doesNotCallApiWhenFoundFromCache() {
            EmployeeDto employee = createEmployee("Иванов", "Иван", "Иванович");
            when(employeeApi.findAll()).thenReturn(List.of(employee));

            employeeFinder.findEmployee("Иванов Иван Иванович");
            employeeFinder.findEmployee("Иванов Иван Иванович");

            verify(employeeApi, times(1)).findAll();
        }

        @Test
        @DisplayName("Обращается к API повторно, если первый поиск не дал результата")
        void callsApiAgainWhenFirstSearchMisses() {
            EmployeeDto employee = createEmployee("Иванов", "Иван", "Иванович");
            when(employeeApi.findAll()).thenReturn(List.of(), List.of(employee));

            employeeFinder.findEmployee("Иванов Иван Иванович");

            verify(employeeApi, times(2)).findAll();
        }
    }
}