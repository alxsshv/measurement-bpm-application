package com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArshinVerificationReportDto {
    private String filename;
    private List<ArshinVerificationRecordDto> records = new ArrayList<>();
}
