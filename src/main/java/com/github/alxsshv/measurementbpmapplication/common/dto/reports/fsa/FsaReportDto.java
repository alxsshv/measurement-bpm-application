package com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FsaReportDto {
    private final String filename;
    private final List<FsaVerificationRecordDto> records;
}
