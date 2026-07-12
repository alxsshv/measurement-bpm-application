package com.github.alxsshv.measurementbpmapplication.reports.service.fsa;

import com.github.alxsshv.arshin_client_starter.api.ArshinClientApi;
import com.github.alxsshv.arshin_client_starter.dto.vri.VriItem;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ArshinResponseException;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportCreationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.github.alxsshv.measurementbpmapplication.reports.utils.fsa.VerificationRecordToVriItemMatcher.getMatchedVriItem;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationRecordSupplementService {

    private final ArshinClientApi arshinClientApi;

    public List<FsaVerificationRecord> updateRecordsFromArshin(List<FsaVerificationRecord> records) {
        String okMessage = "Номера записей о поверке в ФГИС Аршин успешно получены";
        LocalDate minVerificationDate = this.getMinVerificationDate(records);
        LocalDate maxVerificationDate = this.getMaxVerificationDate(records);
        List<VriItem> items = arshinClientApi.getVriItemsByVerificationDatesRange(minVerificationDate, maxVerificationDate);
        log.info("Анализ даных из ФГИС Аршин и сопоставление их с записями о поверке");
        for (FsaVerificationRecord vrfRecord : records) {
            try {
                VriItem item = getMatchedVriItem(vrfRecord, items);
                vrfRecord.setVriId(item.getVriId());
                vrfRecord.setNumberVerification(item.getResultDocnum());
                int result = item.getResultDocnum().contains("С-") ? 1 : 2;
                vrfRecord.setResultVerification(result);
            } catch (ArshinResponseException ex){
                log.error(ex.getMessage());
                throw new ArshinResponseException(ex.getMessage());
            }
        }
        log.info(okMessage);
        return records;
    }


    private LocalDate getMinVerificationDate(List<FsaVerificationRecord> records) {
        return records.stream()
                .map(FsaVerificationRecord::getDateVerification)
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new ReportCreationException("Не удалось определить минимальную дату из диапазона дат проверки средств измерений"));
    }

    private LocalDate getMaxVerificationDate(List<FsaVerificationRecord> records) {
        return records.stream()
                .map(FsaVerificationRecord::getDateVerification)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new ReportCreationException("Не удалось определить максимальную дату из диапазона дат проверки средств измерений"));
    }




}
