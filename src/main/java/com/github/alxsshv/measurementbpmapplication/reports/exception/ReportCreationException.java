package com.github.alxsshv.measurementbpmapplication.reports.exception;

public class ReportCreationException extends RuntimeException {
    public ReportCreationException(String message) {
        super(message);
    }

    public ReportCreationException(String message, Object... args) {
        super(String.format(message, args));
    }
}
