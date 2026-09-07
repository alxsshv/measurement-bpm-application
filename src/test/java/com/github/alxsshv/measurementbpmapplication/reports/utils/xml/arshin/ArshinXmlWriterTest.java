package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin;

import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory.VerificationApplicationFactory;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.VerificationApplication;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArshinXmlWriterTest {

    private final ArshinXmlWriter writer = new ArshinXmlWriter();

    private VerificationApplication createApplication() {
        ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
        vrfRecord.setMi(MeasurementInstrument.builder()
                .miTypeNumber("45201")
                .serialNum("12345")
                .modification("Модификация")
                .owner("ООО Ромашка")
                .build());
        vrfRecord.setVerificationDate(LocalDate.of(2024, Month.JANUARY, 12));
        vrfRecord.setValidDate(LocalDate.of(2025, Month.JANUARY, 12));
        vrfRecord.setVerificationType(1);
        vrfRecord.setApplicable(true);
        vrfRecord.setMiStandards(Set.of());
        vrfRecord.setVerificationMis(Set.of());
        vrfRecord.setEmployee("Иванов Иван Иванович");
        return VerificationApplicationFactory.createApplication(List.of(vrfRecord), "SIGN");
    }

    private String resourceToString(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Nested
    class TestWriteXmlToResourceMethod {

        @Test
        @DisplayName("Формирует XML ресурс с корневым элементом application")
        void writesApplicationRootElement() throws IOException {
            Resource resource = writer.writeXmlToResource(createApplication());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("<gost:application"),
                    "XML должен содержать корневой элемент <gost:application>");
        }

        @Test
        @DisplayName("Формирует XML с элементом result")
        void writesResultElement() throws IOException {
            Resource resource = writer.writeXmlToResource(createApplication());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("<gost:result"), "XML должен содержать элемент <gost:result>");
        }

        @Test
        @DisplayName("Формирует XML с шифром подписи")
        void writesSignCipher() throws IOException {
            Resource resource = writer.writeXmlToResource(createApplication());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("SIGN"));
        }

        @Test
        @DisplayName("Формирует XML с информацией о средстве измерений")
        void writesMiInfo() throws IOException {
            Resource resource = writer.writeXmlToResource(createApplication());

            String xml = resourceToString(resource);
            assertTrue(xml.contains("miInfo"));
            assertTrue(xml.contains("45201"));
            assertTrue(xml.contains("12345"));
        }

        @Test
        @DisplayName("Формирует непустой ресурс")
        void returnsNonEmptyResource() throws IOException {
            Resource resource = writer.writeXmlToResource(createApplication());

            assertNotNull(resource);
            assertTrue(resource.getInputStream().readAllBytes().length > 0);
        }

        @Test
        @DisplayName("Бросает исключение при передаче application со значением null")
        void throwsOnNullApplication() {
            assertThrows(RuntimeException.class, () -> writer.writeXmlToResource(null));
        }
    }
}