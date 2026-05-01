package com.github.alxsshv.measurementbpmapplication.employee.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeAlreadyExistsException extends RuntimeException {
    @Autowired
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }

    public EmployeeAlreadyExistsException(String message, Object... args) {
        super(String.format(message, args));
    }
}
