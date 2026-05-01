package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;


import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.employee.api.EmployeeApi;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.IllegalEmployeeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeFinder {

    private final EmployeeApi employeeApi;

    private List<EmployeeDto> employees = new ArrayList<>();

    public EmployeeDto findEmployee(String fio) {
        if (fio.split(" ").length < 3) {
            throw new IllegalEmployeeException("ФИО поверителя имеет неверный формат." +
                    " Запись должна иметь формат: \"Фамилия Имя Отчество\"");
        }
        try {
            return find(fio);
        } catch (IllegalEmployeeException ex) {
            log.info("Поверитель с ФИО {} не найден. Обновим данные о поверителях и попробуем еще раз", fio);
            updateEmployeesList();
            return find(fio);
        }
    }

    private EmployeeDto find(String fio) {
        Optional<EmployeeDto> employeeOpt = getEmployees().stream()
                .filter(e->(e.surname() + " " + e.name() + " " + e.patronymic()).equals(fio))
                .findAny();
        return employeeOpt.orElseThrow(() -> new IllegalEmployeeException("Ничего не знаю о поверителе %s. " +
                "Возможно данные поверителя указаны неверно. Если всё указано верно добавьте сведения о поверителе" +
                " в раздел \"Список поверителей\" настоящего ПО", fio));
    }

    private List<EmployeeDto> getEmployees() {
        if (employees.isEmpty()) {
            updateEmployeesList();
        }
        return employees;

    }

    private void updateEmployeesList() {
        employees = employeeApi.findAll();
    }
}
