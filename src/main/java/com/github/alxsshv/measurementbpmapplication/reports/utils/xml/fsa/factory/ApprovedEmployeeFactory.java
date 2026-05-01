package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.ApprovedEmployee;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.EmployeeName;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApprovedEmployeeFactory {

    public static ApprovedEmployee createApprovedEmployee(EmployeeDto employee){
        ApprovedEmployee approvedEmployee = new ApprovedEmployee();
        EmployeeName employeeName = new EmployeeName();
        employeeName.setName(employee.name());
        employeeName.setSurname(employee.surname());
        employeeName.setPatronymic(employee.patronymic());
        approvedEmployee.setEmployeeName(employeeName);
        approvedEmployee.setSnils(employee.snils());
        return approvedEmployee;
    }
    
}
