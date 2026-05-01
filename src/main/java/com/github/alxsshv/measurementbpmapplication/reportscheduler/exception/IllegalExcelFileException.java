package com.github.alxsshv.measurementbpmapplication.reportscheduler.exception;

public class IllegalExcelFileException extends RuntimeException {

    public IllegalExcelFileException(String message) {
        super(message);
    }

    public IllegalExcelFileException(String message, Object... args) {
        super(String.format(message, args));
    }
}
