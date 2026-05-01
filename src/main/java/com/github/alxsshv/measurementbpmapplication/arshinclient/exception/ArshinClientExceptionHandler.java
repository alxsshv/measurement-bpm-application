package com.github.alxsshv.measurementbpmapplication.arshinclient.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestControllerAdvice
public class ArshinClientExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceMessage handleArshinClientResponseException(ArshinClientResponseException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceMessage handleHttpServerErrorException(HttpServerErrorException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }




}
