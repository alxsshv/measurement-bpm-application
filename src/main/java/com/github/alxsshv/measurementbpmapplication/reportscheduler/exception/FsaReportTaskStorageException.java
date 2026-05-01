package com.github.alxsshv.measurementbpmapplication.reportscheduler.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class FsaReportTaskStorageException extends RuntimeException {
    @Autowired
    public FsaReportTaskStorageException(String message) {
        super(message);
    }

    public FsaReportTaskStorageException(String message, Object... args) {
        super(String.format(message,args));
    }
}
