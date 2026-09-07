package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MiStandard;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.exception.ExcelParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelArshinVerificationRecordParserTest {

    @Mock
    private EmployeeFinder employeeFinder;

    private ExcelArshinVerificationRecordParser parser;

    @BeforeEach
    void setUp() {
        parser = new ExcelArshinVerificationRecordParser(employeeFinder);
    }

    private EmployeeDto createEmployee() {
        return new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111");
    }

    private ExcelDataObject createExcelObject() {
        ExcelDataObject object = new ExcelDataObject();
        object.setArshinNumber("45201");
        object.setSerialNumber("12345");
        object.setModification("Модификация");
        object.setOwner("-");
        object.setVerificationDate("2024-01-12+10:00");
        object.setValidDate("2025-01-12+10:00");
        object.setVerificationType(1);
        object.setCalibration(true);
        object.setStickerNum("001");
        object.setPassportStamp(true);
        object.setMiStamp(false);
        object.setVerificationDocument("Документ");
        object.setEmployee(" Иванов Иван Иванович ");
        object.setTemperature("20");
        object.setPressure("101");
        object.setHumidity("50");
        return object;
    }

    @Nested
    class TestParseMethod {

        @Test
        @DisplayName("Парсит корректный объект Excel в запись о поверке")
        void parsesValidObject() {
            String employeeFio = "Иванов Иван Иванович";
            ExcelDataObject object = createExcelObject();
            when(employeeFinder.findEmployee(employeeFio)).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertEquals(1, records.size());
            ArshinVerificationRecord vrfRecord = records.get(0);
            assertEquals("45201", vrfRecord.getMi().getMiTypeNumber());
            assertEquals("12345", vrfRecord.getMi().getSerialNum());
            assertEquals("Модификация", vrfRecord.getMi().getModification());
            assertEquals(LocalDate.of(2024, Month.JANUARY, 12), vrfRecord.getVerificationDate());
            assertEquals(LocalDate.of(2025, Month.JANUARY, 12), vrfRecord.getValidDate());
            assertEquals(1, vrfRecord.getVerificationType());
            assertTrue(vrfRecord.isCalibration());
            assertEquals("001", vrfRecord.getStickerNum());
            assertTrue(vrfRecord.isSignPass());
            assertFalse(vrfRecord.isSignMi());
            assertEquals("Документ", vrfRecord.getInstruction());
            assertEquals("Иванов Иван Иванович", vrfRecord.getEmployee());
            assertEquals("20", vrfRecord.getTemperature());
            assertEquals("101", vrfRecord.getPressure());
            assertEquals("50", vrfRecord.getHumidity());
        }

        @Test
        @DisplayName("Устанавливает владельца по умолчанию \"-\", если владелец не указан")
        void setsDefaultOwnerWhenAbsent() {
            ExcelDataObject object = createExcelObject();
            object.setOwner(null);
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertEquals("-", records.get(0).getMi().getOwner());
        }

        @Test
        @DisplayName("Устанавливает указанного владельца")
        void setsProvidedOwner() {
            ExcelDataObject object = createExcelObject();
            object.setOwner("ООО Ромашка");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertEquals("ООО Ромашка", records.get(0).getMi().getOwner());
        }

        @Test
        @DisplayName("Парсит стандарты, разделённые символом '|'")
        void parsesStandardsSeparatedByPipe() {
            ExcelDataObject object = createExcelObject();
            object.setStandards("std1|std2");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            Set<MiStandard> standards = records.get(0).getMiStandards();
            assertEquals(2, standards.size());
            assertTrue(standards.stream().anyMatch(s -> s.getArshinNumber().equals("std1")));
            assertTrue(standards.stream().anyMatch(s -> s.getArshinNumber().equals("std2")));
        }

        @Test
        @DisplayName("Пропускает пустые элементы при парсинге стандартов")
        void skipsEmptyStandardParts() {
            ExcelDataObject object = createExcelObject();
            object.setStandards("std1||");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertEquals(1, records.get(0).getMiStandards().size());
        }

        @Test
        @DisplayName("Возвращает пустое множество стандартов, если стандарты не указаны")
        void returnsEmptyStandardsWhenAbsent() {
            ExcelDataObject object = createExcelObject();
            object.setStandards(null);
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertTrue(records.get(0).getMiStandards().isEmpty());
        }

        @Test
        @DisplayName("Парсит поверяемые средства измерений, разделённые символом '|'")
        void parsesVerificationInstruments() {
            ExcelDataObject object = createExcelObject();
            object.setInstruments("mi1:ser1|mi2:ser2");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            Set<MeasurementInstrument> instruments = records.get(0).getVerificationMis();
            assertEquals(2, instruments.size());
            assertTrue(instruments.stream().anyMatch(mi -> mi.getMiTypeNumber().equals("mi1") && mi.getSerialNum().equals("ser1")));
            assertTrue(instruments.stream().anyMatch(mi -> mi.getMiTypeNumber().equals("mi2") && mi.getSerialNum().equals("ser2")));
        }

        @Test
        @DisplayName("Возвращает пустое множество поверяемых СИ, если они не указаны")
        void returnsEmptyInstrumentsWhenAbsent() {
            ExcelDataObject object = createExcelObject();
            object.setInstruments(null);
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertTrue(records.get(0).getVerificationMis().isEmpty());
        }

        @Test
        @DisplayName("Бросает исключение при некорректном формате поверяемого СИ")
        @SuppressWarnings("java:S5778")
        void throwsOnInvalidInstrumentFormat() {
            ExcelDataObject object = createExcelObject();
            object.setInstruments("invalid-format");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            assertThrows(ExcelParseException.class, () -> parser.parse(List.of(object)));
        }

        @Test
        @DisplayName("Устанавливает applicable=true, если причина непригодности не указана")
        void setsApplicableWhenReasonAbsent() {
            ExcelDataObject object = createExcelObject();
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertTrue(records.get(0).isApplicable());
            assertNull(records.get(0).getInapplicableReason());
        }

        @Test
        @DisplayName("Устанавливает applicable=false и причину, если причина указана")
        void setsInapplicableWhenReasonPresent() {
            ExcelDataObject object = createExcelObject();
            object.setReason("Не соответствует классу");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertFalse(records.get(0).isApplicable());
            assertEquals("Не соответствует классу", records.get(0).getInapplicableReason());
        }

        @Test
        @DisplayName("Устанавливает флаг сокращённой поверки и характеристику")
        void setsShortVerificationData() {
            ExcelDataObject object = createExcelObject();
            object.setShortVrfData("характеристика");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertTrue(records.get(0).isShortVerification());
            assertEquals("характеристика", records.get(0).getShortVerificationCharacteristic());
        }

        @Test
        @DisplayName("Не устанавливает характеристику сокращённой поверки, если её нет")
        void doesNotSetShortVerificationWhenAbsent() {
            ExcelDataObject object = createExcelObject();
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<ArshinVerificationRecord> records = parser.parse(List.of(object));

            assertFalse(records.get(0).isShortVerification());
            assertNull(records.get(0).getShortVerificationCharacteristic());
        }

        @Test
        @DisplayName("Возвращает пустой список для пустого входного списка")
        void parsesEmptyList() {
            List<ArshinVerificationRecord> records = parser.parse(List.of());

            assertTrue(records.isEmpty());
        }
    }
}