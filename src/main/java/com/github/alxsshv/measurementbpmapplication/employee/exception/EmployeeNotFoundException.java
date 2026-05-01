package com.github.alxsshv.measurementbpmapplication.employee.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeNotFoundException extends RuntimeException {

    @Autowired
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
