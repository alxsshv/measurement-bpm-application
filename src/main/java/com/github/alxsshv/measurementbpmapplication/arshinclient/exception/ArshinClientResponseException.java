package com.github.alxsshv.measurementbpmapplication.arshinclient.exception;

import org.springframework.beans.factory.annotation.Autowired;

public class ArshinClientResponseException extends RuntimeException {
    @Autowired
    public ArshinClientResponseException(String message) {
        super(message);
    }

    public ArshinClientResponseException(String message, Object... args) {
        super(String.format(message, args));
    }
}
