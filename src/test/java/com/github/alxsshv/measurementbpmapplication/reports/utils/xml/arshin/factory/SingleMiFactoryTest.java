package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.SingleMi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SingleMiFactoryTest {

    private ArshinVerificationRecord createRecord() {
        ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
        vrfRecord.setMi(MeasurementInstrument.builder()
                .miTypeNumber("45201")
                .serialNum("12345")
                .modification("Модификация")
                .build());
        return vrfRecord;
    }

    @Nested
    class TestCreateSingleMiMethod {

        @Test
        @DisplayName("Заполняет тип, заводской номер и модификацию")
        void createsSingleMiFromRecord() {
            SingleMi singleMi = SingleMiFactory.createSingleMi(createRecord());

            assertEquals("45201", singleMi.getMiTypeNumber());
            assertEquals("12345", singleMi.getManufactureNum());
            assertEquals("Модификация", singleMi.getModification());
        }
    }
}