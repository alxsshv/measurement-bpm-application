package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.MeasurementInstrument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeasurementInstrumentFactoryTest {

    private FsaVerificationRecord createRecord() {
        FsaVerificationRecord vrfRecord = new FsaVerificationRecord();
        vrfRecord.setNumberVerification("prefix/Росаккредитация/abc");
        vrfRecord.setTypeMeasurementInstrument("Вольтметр");
        vrfRecord.setDateVerification(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setResultVerification(1);
        vrfRecord.setEmployee(new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111"));
        return vrfRecord;
    }

    @Nested
    class TestCreateInstrumentMethod {

        @Test
        @DisplayName("Извлекает третий сегмент номера поверки")
        void testCreateInstrumentMethod() {
            MeasurementInstrument instrument = MeasurementInstrumentFactory.createInstrument(createRecord());

            assertTrue(instrument.toString().contains("numberVerification='abc'"));
            assertTrue(instrument.toString().contains("dateVerification='2024-01-12'"));
            assertTrue(instrument.toString().contains("typeMeasuringInstrument='Вольтметр'"));
            assertTrue(instrument.toString().contains("resultVerification=1"));
        }



        @Test
        @DisplayName("Устанавливает указанную дату окончания поверки")
        void setsProvidedDateEndVerification() {
            FsaVerificationRecord vrfRecord = createRecord();
            vrfRecord.setDateEndVerification(LocalDate.of(2025, Month.JANUARY, 12));

            MeasurementInstrument instrument = MeasurementInstrumentFactory.createInstrument(vrfRecord);

            assertTrue(instrument.toString().contains("dateEndVerification='2025-01-12'"));
        }

        @Test
        @DisplayName("Устанавливает дату через 100 лет для бессрочной положительной поверки")
        void setsBigDateEndForIndefinitePositiveVerification() {
            MeasurementInstrument instrument = MeasurementInstrumentFactory.createInstrument(createRecord());

            assertTrue(instrument.toString().contains("dateEndVerification='2124-01-12'"));
        }

        @Test
        @DisplayName("Не устанавливает дату окончания для отрицательной бессрочной поверки")
        void doesNotSetDateEndForIndefiniteNegativeVerification() {
            FsaVerificationRecord vrfRecord = createRecord();
            vrfRecord.setResultVerification(2);

            MeasurementInstrument instrument = MeasurementInstrumentFactory.createInstrument(vrfRecord);

            assertTrue(instrument.toString().contains("dateEndVerification='null'"));
        }

        @Test
        @DisplayName("Бросает исключение, если номер поверки содержит меньше трёх сегментов")
        void throwsWhenNumberVerificationHasLessThanThreeSegments() {
            FsaVerificationRecord vrfRecord = createRecord();
            vrfRecord.setNumberVerification("only-two");

            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> MeasurementInstrumentFactory.createInstrument(vrfRecord));
        }

        @Test
        @DisplayName("Бросает исключение, если номер поверки равен null")
        void throwsWhenNumberVerificationIsNull() {
            FsaVerificationRecord vrfRecord = createRecord();
            vrfRecord.setNumberVerification(null);

            assertThrows(NullPointerException.class,
                    () -> MeasurementInstrumentFactory.createInstrument(vrfRecord));
        }

        @Test
        @DisplayName("Бросает исключение, если дата поверки равна null")
        void throwsWhenVerificationDateIsNull() {
            FsaVerificationRecord vrfRecord = createRecord();
            vrfRecord.setDateVerification(null);

            assertThrows(NullPointerException.class,
                    () -> MeasurementInstrumentFactory.createInstrument(vrfRecord));
        }
    }
}