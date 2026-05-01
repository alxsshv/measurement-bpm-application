package com.github.alxsshv.measurementbpmapplication.reportscheduler.exception;

public class FsaReportTaskProcessingException extends RuntimeException {

    public FsaReportTaskProcessingException(String message) {
        super(message);
    }

    public FsaReportTaskProcessingException(String message, Object... args) {
        super(String.format(message,args));
    }
}
