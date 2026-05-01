package com.github.alxsshv.measurementbpmapplication.reports.utils.fsa;

import com.github.alxsshv.arshin_client_starter.dto.vri.VriItem;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ArshinResponseException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.DateStringConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class VerificationRecordToVriItemMatcher {

    private VerificationRecordToVriItemMatcher() {
    }

    public static VriItem getMatchedVriItem(FsaVerificationRecord vrfRecord, List<VriItem> vriItems) {
        log.info("Поиск данных ФГИС \"Аршин\" для записи о поверке {} зав. №{} поверенного {}",
                vrfRecord.getTypeMeasurementInstrument(), vrfRecord.getSerialNumber(), vrfRecord.getDateVerification());
        List<VriItem> mathedItems = vriItems.stream().filter(item-> matchItems(vrfRecord, item)).toList();
        if (mathedItems.isEmpty()) {
            throw new ArshinResponseException("Для средства измерений [ %s зав. №%s (дата поверки: %s) ] не найдено соответствий в ФГИС \"Аршин\"",
                    vrfRecord.getTypeMeasurementInstrument(), vrfRecord.getSerialNumber(), vrfRecord.getDateVerification());
        }
        if (mathedItems.size() > 1) {
            throw new ArshinResponseException("Для записи [ %s зав. №%s (дата поверки: %s) ] найдено нескольсо соответствий в ФГИС \"Аршин\": \n %s",
                    vrfRecord.getTypeMeasurementInstrument(), vrfRecord.getSerialNumber(), vrfRecord.getDateVerification(), vriItemsToString(mathedItems));
        }
        return mathedItems.get(0);
    }

    private static boolean matchItems(FsaVerificationRecord vrfRecord, VriItem vriItem) {
        LocalDate vriItemVerificationDate = DateStringConverter.parseLocalDateOrGetNull(vriItem.getVerificationDate());
        return vrfRecord.getTypeMeasurementInstrument().equals(vriItem.getMiModification())
                && vrfRecord.getSerialNumber().equals(vriItem.getMiNumber())
                && vrfRecord.getDateVerification().equals(vriItemVerificationDate);
    }

    private static String vriItemsToString(List<VriItem> items) {
        return items.stream()
                .map(i -> String.format("%s зав.№ %s , дата поверки %s", i.getMiModification(), i.getMiNumber(), i.getVerificationDate()))
                .collect(Collectors.joining(";\n"));
    }
}
