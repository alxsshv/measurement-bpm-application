package com.github.alxsshv.measurementbpmapplication.filestorage.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class FileNotFoundException extends RuntimeException {
    @Autowired
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
