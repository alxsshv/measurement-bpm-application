package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.FsaReportMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FsaReportMessageFactoryTest {

    private FsaVerificationRecord createRecord(String numberVerification) {
        FsaVerificationRecord vrfRecord = new FsaVerificationRecord();
        vrfRecord.setNumberVerification("prefix/Росаккредитация/" + numberVerification);
        vrfRecord.setTypeMeasurementInstrument("Вольтметр");
        vrfRecord.setDateVerification(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setResultVerification(1);
        vrfRecord.setEmployee(new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111"));
        return vrfRecord;
    }

    @Nested
    class TestCreateMessageMethod {

        @Test
        @DisplayName("Создаёт сообщение со способом сохранения равным 2")
        void createsMessageWithSaveMethodTwo() {
            FsaReportMessage message = FsaReportMessageFactory.createMessage(
                    List.of(createRecord("verif-1")));

            assertTrue(message.toString().contains("saveMethod=2"));
        }

        @Test
        @DisplayName("Создаёт сообщение с одним средством измерений для одной записи")
        void createsMessageWithOneInstrument() {
            FsaReportMessage message = FsaReportMessageFactory.createMessage(
                    List.of(createRecord("verif-1")));

            assertTrue(message.toString().contains("instruments=["));
        }

        @Test
        @DisplayName("Создаёт сообщение с несколькими средствами измерений")
        void createsMessageWithMultipleInstruments() {
            FsaReportMessage message = FsaReportMessageFactory.createMessage(
                    List.of(createRecord("verif-1"), createRecord("verif-2")));

            assertTrue(message.toString().contains("verif-1"));
            assertTrue(message.toString().contains("verif-2"));
        }

        @Test
        @DisplayName("Создаёт сообщение с пустым списком средств измерений для пустого списка записей")
        void createsMessageForEmptyRecords() {
            FsaReportMessage message = FsaReportMessageFactory.createMessage(List.of());

            assertTrue(message.toString().contains("instruments=[]"));
            assertTrue(message.toString().contains("saveMethod=2"));
        }
    }
}