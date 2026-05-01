package com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.mapper;


import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationRecordDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MiStandard;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArshinVerificationRecordMapper {

    private ArshinVerificationRecordMapper() {
    }

    public static ArshinVerificationRecordDto map(ArshinVerificationRecord vrfRecord) {
        String verificationType = (vrfRecord.getVerificationType() == 1) ? "первичная" : "периодическая";
        Set<String> standardNums = vrfRecord.getMiStandards().stream().map(MiStandard::getArshinNumber).collect(Collectors.toSet());
        String standards = String.join("; ", standardNums);
        var instrumentStrings = vrfRecord.getVerificationMis().stream()
                .map(mi -> String.format("Тип СИ %s зав. № %s", mi.getMiTypeNumber(), mi.getSerialNum())).toList();
        String instruments = String.join("; ", instrumentStrings);
        String applicable = (StringUtils.hasText(vrfRecord.getInapplicableReason())) ? "непригоден":  "пригоден";
        String shortVerification = (StringUtils.hasText(vrfRecord.getShortVerificationCharacteristic())) ? "сокр. объем" : null;

        String vrfDate = (vrfRecord.getVerificationDate() == null) ? null : vrfRecord.getVerificationDate().toString();
        String validDate = (vrfRecord.getValidDate() == null) ? null : vrfRecord.getValidDate().toString();

        return ArshinVerificationRecordDto.builder()
                .miTypeNumber(vrfRecord.getMi().getMiTypeNumber())
                .serialNumber(vrfRecord.getMi().getSerialNum())
                .verificationType(verificationType)
                .verificationDate(vrfDate)
                .validDate(validDate)
                .employee(vrfRecord.getEmployee())
                .miStandards(standards)
                .verificationMis(instruments)
                .instruction(vrfRecord.getInstruction())
                .temperature(vrfRecord.getTemperature())
                .pressure(vrfRecord.getPressure())
                .humidity(vrfRecord.getHumidity())
                .applicable(applicable)
                .inapplicableReason(vrfRecord.getInapplicableReason())
                .shortVrf(shortVerification)
                .shortData(vrfRecord.getShortVerificationCharacteristic())
                .build();
    }

    public static List<ArshinVerificationRecordDto> map(List<ArshinVerificationRecord> vrfRecords) {
        return vrfRecords.stream().map(ArshinVerificationRecordMapper::map).toList();
    }

}
