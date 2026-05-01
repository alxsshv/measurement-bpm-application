package com.github.alxsshv.measurementbpmapplication.reports.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.ExcelParseException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.IllegalEmployeeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ReportsExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceMessage handleIllegalEmployeeException(IllegalEmployeeException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceMessage handleExcelParseException(ExcelParseException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceMessage handleReportFileNotFoundException(ReportFileNotFoundException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceMessage handleArshinResponseException(ArshinResponseException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceMessage handleExcelFileException(ExcelFileException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }


}
