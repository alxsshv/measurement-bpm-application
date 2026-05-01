package com.github.alxsshv.measurementbpmapplication.arshinclient.api;

import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri.VriItem;
import com.github.alxsshv.measurementbpmapplication.arshinclient.service.VriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArshinClientApi {

    private final VriService vriService;

    public List<VriItem> getVriItemsByVerificationDatesRange(LocalDate minVerificationDate, LocalDate maxVerificationDate) {
        return vriService.getVriItemsByVerificationDatesRange(minVerificationDate, maxVerificationDate);
    }
}
