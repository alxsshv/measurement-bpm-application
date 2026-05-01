package com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception;

public class ExcelParseException extends RuntimeException {

    public ExcelParseException(String message) {
        super(message);
    }

    public ExcelParseException(String message, Object...args) {
        super(String.format(message, args));
    }
}
