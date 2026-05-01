package com.github.alxsshv.measurementbpmapplication.employee.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ServiceMessage> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceMessage(ex.getMessage()));
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ServiceMessage> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ServiceMessage(ex.getMessage()));
    }

    @ExceptionHandler(EmployeeStorageException.class)
    public ResponseEntity<ServiceMessage> handleEmployeeNotFoundException(EmployeeStorageException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ServiceMessage(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ServiceMessage> catchConstraintViolationException(ConstraintViolationException ex){
        log.error(ex.getMessage());
        String[] exMessage = ex.getMessage().split(":");
        String errorMessage = exMessage[1];
        if (exMessage[0].contains("parse.excelObjects")){
            errorMessage = createMessage(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServiceMessage(errorMessage));
    }

    private String createMessage(String exceptionMessage){
        String rawNumString;
        int rawNumberBeginIndex = exceptionMessage.indexOf("[") + 1;
        int rawNumberEndIndex = exceptionMessage.indexOf("]");
        if (rawNumberBeginIndex == rawNumberEndIndex){
            rawNumString = exceptionMessage.substring(rawNumberBeginIndex, rawNumberBeginIndex+1);
        } else {
            rawNumString = exceptionMessage.substring(rawNumberBeginIndex, rawNumberEndIndex);
        }
        int rawNumber = Integer.parseInt(rawNumString) + 2;
        return  "Ошибка в файле! Строка № " + rawNumber + ": " + exceptionMessage.split(":")[1];
    }

}
