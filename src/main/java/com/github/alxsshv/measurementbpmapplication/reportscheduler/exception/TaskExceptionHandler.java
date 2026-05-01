package com.github.alxsshv.measurementbpmapplication.reportscheduler.exception;

import com.github.alxsshv.measurementbpmapplication.common.dto.ServiceMessage;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ArshinResponseException;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportFileNotFoundException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.ExcelParseException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.IllegalEmployeeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler({FsaReportTaskProcessingException.class, FsaReportTaskStorageException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceMessage handleFsaReportTaskProcessingException(Exception ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler(FsaReportTaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceMessage handleFsaReportTaskNotFoundException(FsaReportTaskNotFoundException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceMessage handleIllegalExcelFileException(IllegalExcelFileException ex) {
        log.error(ex.getMessage());
        return new ServiceMessage(ex.getMessage());
    }




}
