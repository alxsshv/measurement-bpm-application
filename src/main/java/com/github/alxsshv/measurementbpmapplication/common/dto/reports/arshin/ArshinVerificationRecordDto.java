package com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin;


import com.github.alxsshv.measurementbpmapplication.reports.entity.MeasRecord;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArshinVerificationRecordDto implements MeasRecord {

    private String miTypeNumber;
    private String serialNumber;
    private String verificationType; // 1 - первичная, 2 - периодическая
    private String verificationDate;
    private String validDate;
    private String employee;
    private String miStandards;
    private String verificationMis;// средства измерений применяемые при поверке
    private String instruction;
    private String temperature;
    private String pressure;
    private String humidity;
    private String applicable;
    private String inapplicableReason;
    private String shortVrf;
    private String shortData;


}
