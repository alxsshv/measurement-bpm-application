package com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.mapper;

import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationRecordDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MiStandard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArshinVerificationRecordMapperTest {

    private ArshinVerificationRecord createRecord(int verificationType, String inapplicableReason) {
        ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
        vrfRecord.setMi(MeasurementInstrument.builder()
                .miTypeNumber("45201")
                .serialNum("12345")
                .modification("Модификация")
                .owner("ООО Ромашка")
                .build());
        vrfRecord.setVerificationType(verificationType);
        vrfRecord.setVerificationDate(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setValidDate(LocalDate.of(2025, Month.JANUARY, 12));
        vrfRecord.setEmployee("Иванов Иван Иванович");
        vrfRecord.setMiStandards(Set.of(new MiStandard("std1")));
        vrfRecord.setVerificationMis(Set.of(
                MeasurementInstrument.builder().miTypeNumber("mi1").serialNum("ser1").build()
        ));
        vrfRecord.setInstruction("Документ");
        vrfRecord.setTemperature("20");
        vrfRecord.setPressure("101");
        vrfRecord.setHumidity("50");
        vrfRecord.setInapplicableReason(inapplicableReason);
        return vrfRecord;
    }

    @Nested
    class TestMapMethod {

        @Test
        @DisplayName("Преобразует запись в DTO со всеми полями")
        void mapsRecordToDto() {
            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(createRecord(1, null));

            assertEquals("45201", dto.getMiTypeNumber());
            assertEquals("12345", dto.getSerialNumber());
            assertEquals("2024-01-12", dto.getVerificationDate());
            assertEquals("2025-01-12", dto.getValidDate());
            assertEquals("Иванов Иван Иванович", dto.getEmployee());
            assertEquals("std1", dto.getMiStandards());
            assertEquals("Тип СИ mi1 зав. № ser1", dto.getVerificationMis());
            assertEquals("Документ", dto.getInstruction());
            assertEquals("20", dto.getTemperature());
            assertEquals("101", dto.getPressure());
            assertEquals("50", dto.getHumidity());
        }

        @Test
        @DisplayName("Преобразует тип поверки 1 в \"первичная\"")
        void mapsTypeOneToPrimary() {
            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(createRecord(1, null));

            assertEquals("первичная", dto.getVerificationType());
        }

        @Test
        @DisplayName("Преобразует тип поверки 2 в \"периодическая\"")
        void mapsTypeTwoToPeriodic() {
            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(createRecord(2, null));

            assertEquals("периодическая", dto.getVerificationType());
        }

        @Test
        @DisplayName("Объединяет несколько стандартов разделителем \"; \"")
        void joinsStandardsWithSeparator() {
            ArshinVerificationRecord vrfRecord = createRecord(1, null);
            vrfRecord.setMiStandards(Set.of(new MiStandard("std1"), new MiStandard("std2")));

            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(vrfRecord);

            assertTrue(dto.getMiStandards().contains("std1"));
            assertTrue(dto.getMiStandards().contains("std2"));
        }

        @Test
        @DisplayName("Устанавливает \"пригоден\", если причина непригодности не указана")
        void setsApplicableWhenNoReason() {
            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(createRecord(1, null));

            assertEquals("пригоден", dto.getApplicable());
        }

        @Test
        @DisplayName("Устанавливает \"непригоден\" и причину, если она указана")
        void setsInapplicableWhenReasonPresent() {
            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(
                    createRecord(2, "Не соответствует классу"));

            assertEquals("непригоден", dto.getApplicable());
            assertEquals("Не соответствует классу", dto.getInapplicableReason());
        }

        @Test
        @DisplayName("Устанавливает \"сокр. объем\" при сокращённой поверке")
        void setsShortVrfWhenShortVerification() {
            ArshinVerificationRecord vrfRecord = createRecord(1, null);
            vrfRecord.setShortVerificationCharacteristic("характеристика");

            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(vrfRecord);

            assertEquals("сокр. объем", dto.getShortVrf());
            assertEquals("характеристика", dto.getShortData());
        }

        @Test
        @DisplayName("Не устанавливает короткую характеристику, если её нет")
        void doesNotSetShortVrfWhenAbsent() {
            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(createRecord(1, null));

            assertNull(dto.getShortVrf());
            assertNull(dto.getShortData());
        }

        @Test
        @DisplayName("Оставляет даты null, если они не указаны")
        void leavesDatesNullWhenAbsent() {
            ArshinVerificationRecord vrfRecord = createRecord(1, null);
            vrfRecord.setVerificationDate(null);
            vrfRecord.setValidDate(null);

            ArshinVerificationRecordDto dto = ArshinVerificationRecordMapper.map(vrfRecord);

            assertNull(dto.getVerificationDate());
            assertNull(dto.getValidDate());
        }
    }

    @Nested
    class TestMapListMethod {

        @Test
        @DisplayName("Преобразует список записей в список DTO")
        void mapsRecordListToDtoList() {
            List<ArshinVerificationRecordDto> dtos = ArshinVerificationRecordMapper.map(
                    List.of(createRecord(1, null), createRecord(2, "Брак")));

            assertEquals(2, dtos.size());
            assertEquals("первичная", dtos.get(0).getVerificationType());
            assertEquals("периодическая", dtos.get(1).getVerificationType());
        }

        @Test
        @DisplayName("Возвращает пустой список для пустого списка")
        void mapsEmptyList() {
            List<ArshinVerificationRecordDto> dtos = ArshinVerificationRecordMapper.map(List.of());

            assertEquals(0, dtos.size());
        }
    }
}