package com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.mapper;


import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaVerificationRecordDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.DateStringConverter;

import java.util.List;

public class FsaVerificationRecordDtoMapper {

    private FsaVerificationRecordDtoMapper() {
    }

    public static FsaVerificationRecordDto map(FsaVerificationRecord vrfRecord){
        FsaVerificationRecordDto dto = new FsaVerificationRecordDto();
        dto.setVriId(vrfRecord.getVriId());
        dto.setNumberVerification(vrfRecord.getNumberVerification());
        dto.setDateVerification(DateStringConverter.getStringOrNull(vrfRecord.getDateVerification()));
        dto.setDateEndVerification(DateStringConverter.getStringOrNull(vrfRecord.getDateEndVerification()));
        String result = vrfRecord.getResultVerification() == 1 ? "Пригоден" : "Непригоден";
        dto.setResultVerification(result);
        dto.setTypeMeasurementInstrument(vrfRecord.getTypeMeasurementInstrument());
        dto.setEmployee(vrfRecord.getEmployee());
        return dto;
    }

    public static List<FsaVerificationRecordDto> map(List<FsaVerificationRecord> records) {
        return records.stream().map(FsaVerificationRecordDtoMapper:: map).toList();
    }

}
