package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.BriefProcedure;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultFactoryTest {

    private ArshinVerificationRecord vrfRecord;

    @BeforeEach
    void setUp() {
        vrfRecord = new ArshinVerificationRecord();
        vrfRecord.setMi(MeasurementInstrument.builder()
                .miTypeNumber("45201")
                .serialNum("12345")
                .modification("Модификация")
                .owner(null)
                .build());
        vrfRecord.setVerificationDate(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setValidDate(LocalDate.of(2025, Month.JANUARY, 12));
        vrfRecord.setVerificationType(1);
        vrfRecord.setCalibration(true);
        vrfRecord.setApplicable(true);
        vrfRecord.setMiStandards(Set.of());
        vrfRecord.setVerificationMis(Set.of());
        vrfRecord.setInstruction("Документ");
        vrfRecord.setEmployee("Иванов Иван Иванович");
    }

    @Nested
    class TestCreateResultMethod {

        @Test
        @DisplayName("Заполняет шифр подписи")
        void fillsSignCipher() {
            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertEquals("ABC", result.getSignCipher());
        }

        @Test
        @DisplayName("Подставляет \"Не указан\", когда владелец не указан")
        void setsDefaultMiOwnerWhenAbsent() {
            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertEquals("Не указан", result.getMiOwner());
        }

        @Test
        @DisplayName("Устанавливает указанного владельца")
        void setsProvidedMiOwner() {
            vrfRecord.getMi().setOwner("ООО Ромашка");

            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertEquals("ООО Ромашка", result.getMiOwner());
        }

        @Test
        @DisplayName("Форматирует даты в формат yyyy-MM-dd+03:00")
        void formatsDatesWithOffset() {
            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertEquals("2024-01-12+03:00", result.getVrfDate());
            assertEquals("2025-01-12+03:00", result.getValidDate());
        }

        @Test
        @DisplayName("Оставляет даты null, если они не указаны")
        void leavesDatesNullWhenAbsent() {
            vrfRecord.setVerificationDate(null);
            vrfRecord.setValidDate(null);

            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertNull(result.getVrfDate());
            assertNull(result.getValidDate());
        }

        @Test
        @DisplayName("Устанавливает тип поверки, калибровку и документ")
        void setsTypeCalibrationAndDocTitle() {
            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertEquals(1, result.getVriType());
            assertTrue(result.isCalibration());
            assertEquals("Документ", result.getDocTitle());
        }

        @Test
        @DisplayName("Устанавливает поверителя")
        void setsMetrologist() {
            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertEquals("Иванов Иван Иванович", result.getMetrologist());
        }

        @Test
        @DisplayName("Устанавливает applicable, если средство пригодно")
        void setsApplicableWhenFit() {
            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertNotNull(result.getApplicable());
            assertNull(result.getInapplicable());
        }

        @Test
        @DisplayName("Устанавливает inapplicable, если средство непригодно")
        void setsInapplicableWhenNotFit() {
            vrfRecord.setApplicable(false);
            vrfRecord.setInapplicableReason("Не соответствует классу");

            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertNull(result.getApplicable());
            assertNotNull(result.getInapplicable());
            assertEquals("Не соответствует классу", result.getInapplicable().getReasons());
        }

        @Test
        @DisplayName("Устанавливает сокращённую процедуру при сокращённой поверке")
        void setsBriefProcedureWhenShortVerification() {
            vrfRecord.setShortVerification(true);
            vrfRecord.setShortVerificationCharacteristic("характеристика");

            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            BriefProcedure briefProcedure = result.getBriefProcedure();
            assertNotNull(briefProcedure);
            assertEquals("характеристика", briefProcedure.getCharacteristics());
        }

        @Test
        @DisplayName("Не устанавливает сокращённую процедуру при полной поверке")
        void doesNotSetBriefProcedureWhenFullVerification() {
            vrfRecord.setShortVerification(false);

            Result result = ResultFactory.createResult(vrfRecord, "ABC");

            assertNull(result.getBriefProcedure());
        }
    }
}