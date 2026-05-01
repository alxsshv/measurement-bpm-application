package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;


import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.DateStringConverter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Validated
@Component
@RequiredArgsConstructor
public class ExcelFsaVerificationRecordParser implements ExcelParser {

    private final EmployeeFinder employeeFinder;

    public List<FsaVerificationRecord> parse(@Valid List<ExcelDataObject> arshinObjects){
        List<FsaVerificationRecord> records = new ArrayList<>();
        for (ExcelDataObject object : arshinObjects){
            FsaVerificationRecord vrfRecord = new FsaVerificationRecord();
            vrfRecord.setDateVerification(DateStringConverter.parseLocalDateOrGetNull(object.getVerificationDate()));
            vrfRecord.setDateEndVerification(DateStringConverter.parseLocalDateOrGetNull(object.getValidDate()));
            vrfRecord.setTypeMeasurementInstrument(object.getModification().trim());
            vrfRecord.setSerialNumber(object.getSerialNumber().trim());
            int result = object.getReason()== null ? 1 : 2; //1 - пригоден, 2 - непригоден
            vrfRecord.setResultVerification(result);
            vrfRecord.setEmployee(employeeFinder.findEmployee(object.getEmployee().trim()));
            records.add(vrfRecord);
        }
        return records;
    }
}
