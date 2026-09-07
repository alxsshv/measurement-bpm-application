package com.github.alxsshv.measurementbpmapplication.reports.utils.fsa;

import com.github.alxsshv.arshin_client_starter.dto.vri.VriItem;
import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.exception.ArshinResponseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("java:S5778")
class VerificationRecordToVriItemMatcherTest {

    private FsaVerificationRecord createRecord() {
        FsaVerificationRecord vrfRecord = new FsaVerificationRecord();
        vrfRecord.setTypeMeasurementInstrument("Вольтметр");
        vrfRecord.setSerialNumber("12345");
        vrfRecord.setDateVerification(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setEmployee(new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111"));
        return vrfRecord;
    }

    private VriItem createVriItem(String vriId, String modification, String number, String verificationDate) {
        VriItem item = new VriItem();
        item.setVriId(vriId);
        item.setMiModification(modification);
        item.setMiNumber(number);
        item.setVerificationDate(verificationDate);
        return item;
    }

    @Nested
    class TestGetMatchedVriItemMethod {

        @Test
        @DisplayName("Возвращает единственное совпадение по модификации, номеру и дате")
        void returnsSingleMatch() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem expected = createVriItem("vri-1", "Вольтметр", "12345", "2024-01-12");

            VriItem result = VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(expected));

            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Возвращает совпадение, когда в списке есть и несовпадающие элементы")
        void returnsMatchAmongManyItems() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem other = createVriItem("vri-1", "Амперметр", "99999", "2024-05-01");
            VriItem expected = createVriItem("vri-2", "Вольтметр", "12345", "2024-01-12");

            VriItem result = VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(other, expected));

            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Совпадает с элементом, у которого дата в формате dd.MM.yyyy")
        void matchesItemWithRussianDateFormat() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem item = createVriItem("vri-1", "Вольтметр", "12345", "12.01.2024");

            VriItem result = VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(item));

            assertEquals(item, result);
        }

        @Test
        @DisplayName("Бросает исключение, если совпадений не найдено")
        void throwsWhenNoMatchFound() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem other = createVriItem("vri-1", "Амперметр", "99999", "2024-05-01");

            assertThrows(ArshinResponseException.class,
                    () -> VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(other)));
        }

        @Test
        @DisplayName("Бросает исключение для пустого списка элементов")
        void throwsForEmptyVriItems() {
            FsaVerificationRecord vrfRecord = createRecord();

            assertThrows(ArshinResponseException.class,
                    () -> VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of()));
        }

        @Test
        @DisplayName("Бросает исключение, если найдено несколько совпадений")
        void throwsWhenMultipleMatchesFound() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem first = createVriItem("vri-1", "Вольтметр", "12345", "2024-01-12");
            VriItem second = createVriItem("vri-2", "Вольтметр", "12345", "2024-01-12");

            assertThrows(ArshinResponseException.class,
                    () -> VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(first, second)));
        }

        @Test
        @DisplayName("Не совпадает, если не совпадает модификация")
        void doesNotMatchDifferentModification() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem item = createVriItem("vri-1", "Амперметр", "12345", "2024-01-12");

            assertThrows(ArshinResponseException.class,
                    () -> VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(item)));
        }

        @Test
        @DisplayName("Не совпадает, если не совпадает серийный номер")
        void doesNotMatchDifferentSerialNumber() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem item = createVriItem("vri-1", "Вольтметр", "99999", "2024-01-12");

            assertThrows(ArshinResponseException.class,
                    () -> VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(item)));
        }

        @Test
        @DisplayName("Не совпадает, если не совпадает дата поверки")
        void doesNotMatchDifferentDate() {
            FsaVerificationRecord vrfRecord = createRecord();
            VriItem item = createVriItem("vri-1", "Вольтметр", "12345", "2024-05-01");

            assertThrows(ArshinResponseException.class,
                    () -> VerificationRecordToVriItemMatcher.getMatchedVriItem(vrfRecord, List.of(item)));
        }
    }
}