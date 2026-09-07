package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Conditions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConditionsFactoryTest {

    @Nested
    class TestCreateConditionsMethod {

        @Test
        @DisplayName("Заполняет температуру, давление и влажность")
        void createsConditionsWithAllValues() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setTemperature("20");
            vrfRecord.setPressure("101");
            vrfRecord.setHumidity("50");

            Conditions conditions = ConditionsFactory.createConditions(vrfRecord);

            assertEquals("20", conditions.getTemperature());
            assertEquals("101", conditions.getPressure());
            assertEquals("50", conditions.getHumidity());
        }

        @Test
        @DisplayName("Сохраняет null значения условий")
        void keepsNullValues() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();

            Conditions conditions = ConditionsFactory.createConditions(vrfRecord);

            assertEquals(null, conditions.getTemperature());
            assertEquals(null, conditions.getPressure());
            assertEquals(null, conditions.getHumidity());
        }
    }
}