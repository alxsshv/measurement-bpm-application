package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.MiInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MiInfoFactoryTest {

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
    class TestCreateMiInfoMethod {

        @Test
        @DisplayName("Создаёт miInfo с заполненным singleMI")
        void createsMiInfoWithSingleMi() {
            MiInfo miInfo = MiInfoFactory.createMiInfo(createRecord());

            assertNotNull(miInfo.getSingleMi());
            assertEquals("45201", miInfo.getSingleMi().getMiTypeNumber());
            assertEquals("12345", miInfo.getSingleMi().getManufactureNum());
        }
    }
}