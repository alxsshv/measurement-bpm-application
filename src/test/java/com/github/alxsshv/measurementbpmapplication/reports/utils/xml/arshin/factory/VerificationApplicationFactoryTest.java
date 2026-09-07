package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.VerificationApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationApplicationFactoryTest {

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

    @Nested
    class TestCreateApplicationMethod {

        @Test
        @DisplayName("Создаёт application с одним result для одной записи")
        void createsApplicationWithOneResult() {
            VerificationApplication application = VerificationApplicationFactory.createApplication(
                    List.of(createRecord("12345")), "SIGN");

            assertEquals(1, application.getResults().size());
        }

        @Test
        @DisplayName("Создаёт application с несколькими result")
        void createsApplicationWithMultipleResults() {
            VerificationApplication application = VerificationApplicationFactory.createApplication(
                    List.of(createRecord("12345"), createRecord("67890")), "SIGN");

            assertEquals(2, application.getResults().size());
        }

        @Test
        @DisplayName("Передаёт шифр подписи в каждый result")
        void passesSignCipherToResults() {
            VerificationApplication application = VerificationApplicationFactory.createApplication(
                    List.of(createRecord("12345"), createRecord("67890")), "SIGN");

            assertTrue(application.getResults().stream().allMatch(r -> "SIGN".equals(r.getSignCipher())));
        }

        @Test
        @DisplayName("Создаёт application с пустым списком result для пустого списка записей")
        void createsApplicationForEmptyRecords() {
            VerificationApplication application = VerificationApplicationFactory.createApplication(List.of(), "SIGN");

            assertTrue(application.getResults().isEmpty());
        }
    }
}