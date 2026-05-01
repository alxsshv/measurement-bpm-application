package com.github.alxsshv.measurementbpmapplication.reports.exception;

public class ArshinResponseException extends RuntimeException {
    public ArshinResponseException(String message) {
        super(message);
    }

    public ArshinResponseException(String message, Object...args) {
        super(String.format(message, args));
    }
}
