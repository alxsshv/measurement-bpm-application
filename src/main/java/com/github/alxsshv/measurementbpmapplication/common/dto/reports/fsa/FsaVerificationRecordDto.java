package com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FsaVerificationRecordDto {
    private String vriId;
    private String numberVerification;
    private String dateVerification;
    private String dateEndVerification;
    private String typeMeasurementInstrument;
    private EmployeeDto employee;
    private String resultVerification;
}
