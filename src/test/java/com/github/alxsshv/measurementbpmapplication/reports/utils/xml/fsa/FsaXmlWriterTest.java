package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa;

import com.github.alxsshv.measurementbpmapplication.common.dto.employee.EmployeeDto;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory.FsaReportMessageFactory;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.FsaReportMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FsaXmlWriterTest {

    private final FsaXmlWriter writer = new FsaXmlWriter();

    private FsaReportMessage createMessage() {
        FsaVerificationRecord vrfRecord = new FsaVerificationRecord();
        vrfRecord.setNumberVerification("prefix/Росаккредитация/verif-1");
        vrfRecord.setTypeMeasurementInstrument("Вольтметр");
        vrfRecord.setDateVerification(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setResultVerification(1);
        vrfRecord.setEmployee(new EmployeeDto("1", "Иван", "Иванов", "Иванович", "11111111111"));
        return FsaReportMessageFactory.createMessage(List.of(vrfRecord));
    }

    private String resourceToString(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Nested
    class TestWriteXmlToResourceMethod {

        @Test
        @DisplayName("Формирует XML ресурс с корневым элементом Message")
        void writesMessageRootElement() throws IOException {
            Resource resource = writer.writeXmlToResource(createMessage());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("<Message"), "XML должен содержать корневой элемент <Message>");
        }

        @Test
        @DisplayName("Формирует XML со способом сохранения равным 2")
        void writesSaveMethodTwo() throws IOException {
            Resource resource = writer.writeXmlToResource(createMessage());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("SaveMethod"), "XML должен содержать элемент SaveMethod");
            assertTrue(xml.contains(">2<"), "XML должен содержать значение 2");
        }

        @Test
        @DisplayName("Формирует XML с данными средства измерений")
        void writesInstrumentData() throws IOException {
            Resource resource = writer.writeXmlToResource(createMessage());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("VerificationMeasuringInstrument"));
            assertTrue(xml.contains("NumberVerification"));
            assertTrue(xml.contains(">verif-1<"));
        }

        @Test
        @DisplayName("Формирует XML с данными о поверителе")
        void writesEmployeeData() throws IOException {
            Resource resource = writer.writeXmlToResource(createMessage());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("ApprovedEmployee"));
            assertTrue(xml.contains("SNILS"));
            assertTrue(xml.contains("11111111111"));
        }

        @Test
        @DisplayName("Формирует непустой ресурс")
        void returnsNonEmptyResource() throws IOException {
            Resource resource = writer.writeXmlToResource(createMessage());

            assertNotNull(resource);
            assertTrue(resource.getInputStream().readAllBytes().length > 0);
        }

        @Test
        @DisplayName("Бросает исключение при передаче message со значением null")
        void throwsOnNullMessage() {
            assertThrows(RuntimeException.class, () -> writer.writeXmlToResource(null));
        }
    }
}