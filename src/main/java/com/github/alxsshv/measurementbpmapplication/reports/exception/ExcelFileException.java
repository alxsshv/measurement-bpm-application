package com.github.alxsshv.measurementbpmapplication.reports.exception;

public class ExcelFileException extends RuntimeException {

    public ExcelFileException(String message) {
        super(message);
    }

    public ExcelFileException(String message, Object... args) {
        super(String.format(message, args));
    }
}
