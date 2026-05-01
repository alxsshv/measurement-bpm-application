package com.github.alxsshv.measurementbpmapplication.reports.exception;

public class ReportFileNotFoundException extends RuntimeException {

    public ReportFileNotFoundException(String message) {
        super(message);
    }

    public ReportFileNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
