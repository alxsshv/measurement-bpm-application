package com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class IllegalEmployeeException extends RuntimeException {
    @Autowired
    public IllegalEmployeeException(String message) {
        super(message);
    }

    public IllegalEmployeeException(String message, Object... args) {
        super(String.format(message, args));
    }
}
