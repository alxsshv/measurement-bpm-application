package com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.mapper;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaVerificationRecordDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FsaVerificationRecordDtoMapperTest {

    private FsaVerificationRecord createRecord(int resultVerification) {
        return new FsaVerificationRecord(
                "vri-1",
                "verif-1",
                LocalDate.of(2024, Month.JANUARY, 12),
                LocalDate.of(2025, Month.JANUARY, 12),
                "Вольтметр",
                new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111"),
                resultVerification,
                "12345"
        );
    }

    @Nested
    class TestMapMethod {

        @Test
        @DisplayName("Преобразует запись в DTO со всеми полями")
        void mapsRecordToDto() {
            FsaVerificationRecordDto dto = FsaVerificationRecordDtoMapper.map(createRecord(1));

            assertEquals("vri-1", dto.getVriId());
            assertEquals("verif-1", dto.getNumberVerification());
            assertEquals("2024-01-12", dto.getDateVerification());
            assertEquals("2025-01-12", dto.getDateEndVerification());
            assertEquals("Вольтметр", dto.getTypeMeasurementInstrument());
            assertEquals("Иван", dto.getEmployee().name());
            assertEquals("1", dto.getEmployee().id());
        }

        @Test
        @DisplayName("Преобразует результат 1 в \"Пригоден\"")
        void mapsResultOneToPrigoden() {
            FsaVerificationRecordDto dto = FsaVerificationRecordDtoMapper.map(createRecord(1));

            assertEquals("Пригоден", dto.getResultVerification());
        }

        @Test
        @DisplayName("Преобразует результат 2 в \"Непригоден\"")
        void mapsResultTwoToNeprigoden() {
            FsaVerificationRecordDto dto = FsaVerificationRecordDtoMapper.map(createRecord(2));

            assertEquals("Непригоден", dto.getResultVerification());
        }

        @Test
        @DisplayName("Оставляет даты null, если они не указаны")
        void leavesDatesNullWhenAbsent() {
            FsaVerificationRecord vrfRecord = createRecord(1);
            vrfRecord.setDateVerification(null);
            vrfRecord.setDateEndVerification(null);

            FsaVerificationRecordDto dto = FsaVerificationRecordDtoMapper.map(vrfRecord);

            assertNull(dto.getDateVerification());
            assertNull(dto.getDateEndVerification());
        }
    }

    @Nested
    class TestMapListMethod {

        @Test
        @DisplayName("Преобразует список записей в список DTO")
        void mapsRecordListToDtoList() {
            List<FsaVerificationRecordDto> dtos = FsaVerificationRecordDtoMapper.map(
                    List.of(createRecord(1), createRecord(2)));

            assertEquals(2, dtos.size());
            assertEquals("Пригоден", dtos.get(0).getResultVerification());
            assertEquals("Непригоден", dtos.get(1).getResultVerification());
        }

        @Test
        @DisplayName("Возвращает пустой список для пустого списка")
        void mapsEmptyList() {
            List<FsaVerificationRecordDto> dtos = FsaVerificationRecordDtoMapper.map(List.of());

            assertEquals(0, dtos.size());
        }
    }
}