package com.github.alxsshv.measurementbpmapplication.arshinclient.service;

import com.github.alxsshv.measurementbpmapplication.arshinclient.ArshinHttpClient;
import com.github.alxsshv.measurementbpmapplication.arshinclient.VerificationRequestBuilder;
import com.github.alxsshv.measurementbpmapplication.common.config.OrganizationConfig;
import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ArshinResponseException;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri.VriItem;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri.VriResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.github.alxsshv.measurementbpmapplication.reports.utils.WaitManager.waitMillis;

@Slf4j
@Component
@RequiredArgsConstructor
public class VriService {
    private static final int MAX_RECORDS_PER_REQUEST = 100;

    private final OrganizationConfig organization;
    private final PathsConfig paths;

    public List<VriItem> getVriItemsByVerificationDatesRange(LocalDate minVerificationDate, LocalDate maxVerificationDate) {
        if (organization.title().isEmpty()){
            throw new ArshinResponseException("Не указано  наименование организации в файле конфигурации." +
                    " Наименование должно быть указано также как и в ФГИС \"Аршин\" в разделе поверка СИ");
        }
        log.info("Получение данных ФГИС \"Аршин\" для записей о поверке средств измерений в периоде от {} до {}",
                minVerificationDate ,maxVerificationDate);
        String firstVerificationRequest = buildVerificationRequest(minVerificationDate, maxVerificationDate, 0);
        VriResult firstResult = ArshinHttpClient.getVerificationItems(firstVerificationRequest, 0);
        log.info("Количество записей за период с {} до {} составляет {}", minVerificationDate, maxVerificationDate, firstResult.getCount());
        List<VriItem> items = new ArrayList<>(firstResult.getItems());
        if (firstResult.getCount() > MAX_RECORDS_PER_REQUEST) {
            waitMillis(5000);
            for (int i = MAX_RECORDS_PER_REQUEST; i <= firstResult.getCount(); i = i + MAX_RECORDS_PER_REQUEST) {
                String verificationRequest = buildVerificationRequest(minVerificationDate, maxVerificationDate, i);
                VriResult result = ArshinHttpClient.getVerificationItems(verificationRequest, 0);
                items.addAll(result.getItems());
                waitMillis(5000);
            }
        }
        return items;
    }

    private String buildVerificationRequest(LocalDate minVerificationDate, LocalDate maxVerificationDate, int start) {
        return new VerificationRequestBuilder()
                .start(start)
                .rows(MAX_RECORDS_PER_REQUEST)
                .uri(paths.arshinVriUri())
                .orgTitle(organization.title())
                .verificationDateRange(minVerificationDate, maxVerificationDate)
                .build();
    }


}
