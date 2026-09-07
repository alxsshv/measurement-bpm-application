package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelFsaVerificationRecordParserTest {

    @Mock
    private EmployeeFinder employeeFinder;

    private ExcelFsaVerificationRecordParser parser;

    @BeforeEach
    void setUp() {
        parser = new ExcelFsaVerificationRecordParser(employeeFinder);
    }

    private EmployeeDto createEmployee() {
        return new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111");
    }

    private ExcelDataObject createExcelObject() {
        ExcelDataObject object = new ExcelDataObject();
        object.setSerialNumber(" 12345 ");
        object.setModification(" Вольтметр ");
        object.setVerificationDate("2024-01-12+10:00");
        object.setValidDate("2025-01-12+10:00");
        object.setVerificationType(1);
        object.setCalibration(false);
        object.setPassportStamp(true);
        object.setMiStamp(false);
        object.setVerificationDocument("Документ");
        object.setEmployee(" Иванов Иван Иванович ");
        return object;
    }

    @Nested
    class TestParseMethod {

        @Test
        @DisplayName("Парсит корректный объект Excel в запись о поверке")
        void parsesValidObject() {
            EmployeeDto employee = createEmployee();
            ExcelDataObject object = createExcelObject();
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(employee);

            List<FsaVerificationRecord> records = parser.parse(List.of(object));

            assertEquals(1, records.size());
            FsaVerificationRecord vrfRecord = records.get(0);
            assertEquals(LocalDate.of(2024, Month.JANUARY, 12), vrfRecord.getDateVerification());
            assertEquals(LocalDate.of(2025, Month.JANUARY, 12), vrfRecord.getDateEndVerification());
            assertEquals("Вольтметр", vrfRecord.getTypeMeasurementInstrument());
            assertEquals("12345", vrfRecord.getSerialNumber());
            assertEquals(employee, vrfRecord.getEmployee());
        }

        @Test
        @DisplayName("Устанавливает результат 1 (пригоден), если причина непригодности не указана")
        void setsResultOneWhenReasonAbsent() {
            ExcelDataObject object = createExcelObject();
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<FsaVerificationRecord> records = parser.parse(List.of(object));

            assertEquals(1, records.get(0).getResultVerification());
        }

        @Test
        @DisplayName("Устанавливает результат 2 (непригоден), если указана причина непригодности")
        void setsResultTwoWhenReasonPresent() {
            ExcelDataObject object = createExcelObject();
            object.setReason("Брак");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<FsaVerificationRecord> records = parser.parse(List.of(object));

            assertEquals(2, records.get(0).getResultVerification());
        }

        @Test
        @DisplayName("Оставляет даты null, если они не указаны в объекте Excel")
        void leavesDatesNullWhenAbsent() {
            ExcelDataObject object = createExcelObject();
            object.setVerificationDate(null);
            object.setValidDate(null);
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<FsaVerificationRecord> records = parser.parse(List.of(object));

            assertEquals(null, records.get(0).getDateVerification());
            assertEquals(null, records.get(0).getDateEndVerification());
        }

        @Test
        @DisplayName("Парсит несколько записей")
        void parsesMultipleObjects() {
            ExcelDataObject object1 = createExcelObject();
            object1.setModification(" Вольтметр ");
            object1.setSerialNumber(" 1 ");
            ExcelDataObject object2 = createExcelObject();
            object2.setModification(" Амперметр ");
            object2.setSerialNumber(" 2 ");
            when(employeeFinder.findEmployee("Иванов Иван Иванович")).thenReturn(createEmployee());

            List<FsaVerificationRecord> records = parser.parse(List.of(object1, object2));

            assertEquals(2, records.size());
            assertEquals("Вольтметр", records.get(0).getTypeMeasurementInstrument());
            assertEquals("Амперметр", records.get(1).getTypeMeasurementInstrument());
        }

        @Test
        @DisplayName("Возвращает пустой список для пустого входного списка")
        void parsesEmptyList() {
            List<FsaVerificationRecord> records = parser.parse(List.of());

            assertTrue(records.isEmpty());
        }
    }
}