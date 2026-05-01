package com.github.alxsshv.measurementbpmapplication.reports.entity.fsa;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.MeasRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FsaVerificationRecord implements MeasRecord {
    private String vriId;
    private String numberVerification; // Номер записи о поверке
    private LocalDate dateVerification;  //дата поверки
    private LocalDate dateEndVerification; //дата действия поверки
    private String typeMeasurementInstrument; //модификация
    private EmployeeDto employee; // поверитель
    private int resultVerification; // результат поверки
    private String serialNumber;
}
