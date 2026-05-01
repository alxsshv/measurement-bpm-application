package com.github.alxsshv.measurementbpmapplication.reports.entity.arshin;


import com.github.alxsshv.measurementbpmapplication.reports.entity.MeasRecord;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArshinVerificationRecord implements MeasRecord {

    private MeasurementInstrument mi;
    private int verificationType; // 1 - первичная, 2 - периодическая
    private LocalDate verificationDate;
    private LocalDate validDate;
    private String arshinVerificationNumber;
    private String inapplicableReason;
    private String employee;
    private Set<MiStandard> miStandards;
    private Set<MeasurementInstrument> verificationMis;// средства измерений применяемые при поверке
    private boolean  calibration;
    private String stickerNum;
    private boolean signPass;
    private boolean signMi;
    private boolean shortVerification;
    private String shortVerificationCharacteristic;
    private String instruction;
    private boolean applicable;
    private String temperature;
    private String pressure;
    private String humidity;


    @Override
    public String toString() {
        return "VerificationRecord{" +
                "verificationType=" + verificationType +
                ", verificationDate=" + verificationDate +
                ", validDate=" + validDate +
                ", applicable=" + applicable +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", arshinVerificationNumber='" + arshinVerificationNumber + '\'' +
                ", inapplicableReason='" + inapplicableReason + '\'' +
                ", mi=" + mi.toString() +
                ", employee=" + employee +
                ", miStandards=" + miStandards +
                ", verificationMis=" + verificationMis +
                ", calibration=" + calibration +
                ", stickerNum='" + stickerNum + '\'' +
                ", signPass=" + signPass +
                ", signMi=" + signMi +
                ", shortVerification=" + shortVerification +
                ", shortVerificationCharacteristic='" + shortVerificationCharacteristic + '\'' +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}
