package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.ApprovedEmployee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApprovedEmployeeFactoryTest {

    @Nested
    class TestCreateApprovedEmployeeMethod {

        @Test
        @DisplayName("Заполняет имя, фамилию, отчество и СНИЛС из EmployeeDto")
        void createsApprovedEmployeeFromDto() {
            EmployeeDto employee = new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111");

            ApprovedEmployee approvedEmployee = ApprovedEmployeeFactory.createApprovedEmployee(employee);

            assertNotNull(approvedEmployee.getEmployeeName());
            assertEquals("Иванов", approvedEmployee.getEmployeeName().getSurname());
            assertEquals("Иван", approvedEmployee.getEmployeeName().getName());
            assertEquals("Иванович", approvedEmployee.getEmployeeName().getPatronymic());
            assertEquals("11111111111", approvedEmployee.getSnils());
        }
    }
}