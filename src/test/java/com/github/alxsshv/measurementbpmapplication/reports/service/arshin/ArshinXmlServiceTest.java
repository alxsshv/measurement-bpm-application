package com.github.alxsshv.measurementbpmapplication.reports.service.arshin;

import com.github.alxsshv.measurementbpmapplication.common.config.OrganizationConfig;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.ArshinXmlWriter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.VerificationApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArshinXmlServiceTest {

    @Mock
    private ArshinXmlWriter arshinXmlWriter;

    @Nested
    class TestCreateXmlMethod {

        @Test
        @DisplayName("Формирует xml из записей и передаёт шифр подписи")
        void createsXmlFromRecordsAndPassesSignCipher() {
            Resource expectedResource = new ByteArrayResource("xml".getBytes());
            when(arshinXmlWriter.writeXmlToResource(any(VerificationApplication.class))).thenReturn(expectedResource);
            ArshinXmlService arshinXmlService = new ArshinXmlService(arshinXmlWriter,
                    new OrganizationConfig("SIGN", "Организация"));

            Resource result = arshinXmlService.createXml(List.of(createRecord("12345")));

            assertSame(expectedResource, result);
            ArgumentCaptor<VerificationApplication> applicationCaptor = ArgumentCaptor.forClass(VerificationApplication.class);
            verify(arshinXmlWriter).writeXmlToResource(applicationCaptor.capture());
            VerificationApplication application = applicationCaptor.getValue();
            assertEquals(1, application.getResults().size());
            assertEquals("SIGN", application.getResults().get(0).getSignCipher());
        }

        @Test
        @DisplayName("Формирует xml для пустого списка записей")
        void createsXmlForEmptyRecords() {
            Resource expectedResource = new ByteArrayResource("xml".getBytes());
            when(arshinXmlWriter.writeXmlToResource(any(VerificationApplication.class))).thenReturn(expectedResource);
            ArshinXmlService arshinXmlService = new ArshinXmlService(arshinXmlWriter,
                    new OrganizationConfig("SIGN", "Организация"));

            Resource result = arshinXmlService.createXml(List.of());

            assertSame(expectedResource, result);
            ArgumentCaptor<VerificationApplication> applicationCaptor = ArgumentCaptor.forClass(VerificationApplication.class);
            verify(arshinXmlWriter).writeXmlToResource(applicationCaptor.capture());
            assertEquals(0, applicationCaptor.getValue().getResults().size());
        }
    }

    private ArshinVerificationRecord createRecord(String serialNum) {
        ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
        vrfRecord.setMi(MeasurementInstrument.builder()
                .miTypeNumber("45201")
                .serialNum(serialNum)
                .modification("Модификация")
                .build());
        vrfRecord.setVerificationDate(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setValidDate(LocalDate.of(2025, Month.JANUARY, 12));
        vrfRecord.setVerificationType(1);
        vrfRecord.setApplicable(true);
        vrfRecord.setMiStandards(Set.of());
        vrfRecord.setVerificationMis(Set.of());
        return vrfRecord;
    }
}