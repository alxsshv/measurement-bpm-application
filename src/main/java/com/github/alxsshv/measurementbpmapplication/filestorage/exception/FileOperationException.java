package com.github.alxsshv.measurementbpmapplication.filestorage.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class FileOperationException extends RuntimeException {
    @Autowired
    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Object... args) {
        super(String.format(message, args));
    }
}
