package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;


import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MiStandard;
import com.github.alxsshv.measurementbpmapplication.reports.utils.DateStringConverter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.ExcelParseException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Validated
@Component
@RequiredArgsConstructor
public class ExcelArshinVerificationRecordParser implements ExcelParser {
    private static final String DEFAULT_OWNER_VALUE = "-";
    private static final String MI_TYPE_AND_SERIAL_NUMS_DELIMITER = ":";
    private static final String STANDARDS_DELIMITER = "\\|";
    private static final String VERIFICATION_INSTRUMENTS_DELIMITER = "\\|";
    private static final int EXCEL_COLUMN_HEADER_OFFSET = 2;
    private static final int ACCEPTABLE_NUMBER_OF_INSTRUMENT_DEFINITION_PARTS = 2;

    private final EmployeeFinder employeeFinder;

    public List<ArshinVerificationRecord> parse(@Valid List<ExcelDataObject> excelObjects){
        List<ArshinVerificationRecord> records = new ArrayList<>();
        for (int i = 0; i < excelObjects.size(); i++) {
            ExcelDataObject object = excelObjects.get(i);
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setMi(parseMi(object));
            vrfRecord.setVerificationDate(DateStringConverter.parseLocalDateOrGetNull(object.getVerificationDate()));
            vrfRecord.setValidDate(DateStringConverter.parseLocalDateOrGetNull(object.getValidDate()));
            vrfRecord.setVerificationType(object.getVerificationType());
            vrfRecord.setCalibration(object.isCalibration());
            vrfRecord.setStickerNum(object.getStickerNum());
            vrfRecord.setSignPass(object.isPassportStamp());
            vrfRecord.setSignMi(object.isMiStamp());
            vrfRecord.setInstruction(object.getVerificationDocument());
            EmployeeDto employee = employeeFinder.findEmployee(object.getEmployee().trim());
            vrfRecord.setEmployee(String.format("%s %s %s", employee.surname(), employee.name(), employee.patronymic()));
            vrfRecord.setMiStandards(parseStandards(object));
            vrfRecord.setVerificationMis(parseVerificationInstruments(object, i + EXCEL_COLUMN_HEADER_OFFSET));
            boolean applicable = object.getReason() == null;
            vrfRecord.setApplicable(applicable);
            if (!applicable) {
                vrfRecord.setInapplicableReason(object.getReason());
            }
            vrfRecord.setTemperature(object.getTemperature());
            vrfRecord.setPressure(object.getPressure());
            vrfRecord.setHumidity(object.getHumidity());
            boolean shortVerification = object.getShortVrfData() != null;
            vrfRecord.setShortVerification(shortVerification);
            if (shortVerification) {
                vrfRecord.setShortVerificationCharacteristic(object.getShortVrfData());
            }
            records.add(vrfRecord);
        }
        return records;
    }

    private MeasurementInstrument parseMi(ExcelDataObject arshinData) {
        String owner = (StringUtils.hasText(arshinData.getOwner())) ? arshinData.getOwner() : DEFAULT_OWNER_VALUE;
        return MeasurementInstrument.builder()
                .miTypeNumber(arshinData.getArshinNumber())
                .serialNum(arshinData.getSerialNumber())
                .modification(arshinData.getModification())
                .owner(owner)
                .build();
    }

    private Set<MiStandard> parseStandards(ExcelDataObject arshinData) {
        if (arshinData.getStandards() == null) {
            return new HashSet<>();
        }
        Set<MiStandard> standards = new HashSet<>();
        String[] standardNums = arshinData.getStandards().split(STANDARDS_DELIMITER);
        for (String standardNum : standardNums) {
            if (StringUtils.hasText(standardNum)) {
                standards.add(new MiStandard(standardNum));
            }
        }
        return standards;
    }

    private Set<MeasurementInstrument> parseVerificationInstruments(ExcelDataObject arshinData, int excelRowIndex) {
        Set<MeasurementInstrument> instruments = new HashSet<>();
        if (arshinData.getInstruments() == null) {
            return new HashSet<>();
        }
        String[] instrumentDefinitions = arshinData.getInstruments().split(VERIFICATION_INSTRUMENTS_DELIMITER);

        for (String instrumentDefinition : instrumentDefinitions) {
            Optional<MeasurementInstrument> miOpt = parseVerificationInstrument(instrumentDefinition, excelRowIndex);
            miOpt.ifPresent(instruments::add);
        }
        return instruments;
    }

    private Optional<MeasurementInstrument> parseVerificationInstrument(String instrumentDefinition, int excelRowIndex) {
        if (StringUtils.hasText(instrumentDefinition)) {
            String[] instrumentDetails = getInstrumentDefinitionDetails(instrumentDefinition, excelRowIndex);
            var mi = MeasurementInstrument.builder()
                    .miTypeNumber(instrumentDetails[0])
                    .serialNum(instrumentDetails[1])
                    .build();
            return Optional.ofNullable(mi);
        }
        return Optional.empty();
    }

    private static String[] getInstrumentDefinitionDetails(String instrumentDefinition, int excelRowIndex) {
        String[] instrumentDetails = instrumentDefinition.trim().split(MI_TYPE_AND_SERIAL_NUMS_DELIMITER);
        if (instrumentDetails.length != ACCEPTABLE_NUMBER_OF_INSTRUMENT_DEFINITION_PARTS) {
            throw new ExcelParseException("Excel: Строка № %s: Неверно указано средство измерений, применяемое при поверке. " +
                    "Необходимо указывать номер в госреестре, затем символ \"%s\", далее заводской номер СИ. " +
                    "Средства измерений следует разделять символом \"|\"", excelRowIndex, MI_TYPE_AND_SERIAL_NUMS_DELIMITER);
        }
        return instrumentDetails;
    }
}
