package com.github.alxsshv.measurementbpmapplication.reportscheduler.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class FsaReportTaskNotFoundException extends RuntimeException {
    @Autowired
    public FsaReportTaskNotFoundException(String message) {
        super(message);
    }

    public FsaReportTaskNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
