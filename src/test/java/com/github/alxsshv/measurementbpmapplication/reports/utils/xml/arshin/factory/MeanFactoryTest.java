package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MiStandard;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Mean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeanFactoryTest {

    @Nested
    class TestCreateMeansMethod {

        @Test
        @DisplayName("Создаёт список эталонов из стандартов")
        void createsMietaListFromStandards() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setMiStandards(Set.of(new MiStandard("std1"), new MiStandard("std2")));
            vrfRecord.setVerificationMis(Set.of());

            Mean mean = MeanFactory.createMeans(vrfRecord);

            assertEquals(2, mean.getMiEtaList().size());
            assertTrue(mean.getMiEtaList().stream().anyMatch(m -> m.getNumber().equals("std1")));
            assertTrue(mean.getMiEtaList().stream().anyMatch(m -> m.getNumber().equals("std2")));
        }

        @Test
        @DisplayName("Создаёт пустой список эталонов, если стандарты не указаны")
        void createsEmptyMietaListWhenNoStandards() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setMiStandards(Set.of());
            vrfRecord.setVerificationMis(Set.of());

            Mean mean = MeanFactory.createMeans(vrfRecord);

            assertTrue(mean.getMiEtaList().isEmpty());
        }

        @Test
        @DisplayName("Не создаёт секцию mis, если поверяемые СИ не указаны")
        void doesNotCreateMisWhenNoInstruments() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setMiStandards(Set.of());
            vrfRecord.setVerificationMis(Set.of());

            Mean mean = MeanFactory.createMeans(vrfRecord);

            assertNull(mean.getMis());
        }

        @Test
        @DisplayName("Создаёт секцию mis с поверяемыми средствами измерений")
        void createsMisWithInstruments() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setMiStandards(Set.of());
            vrfRecord.setVerificationMis(Set.of(
                    MeasurementInstrument.builder().miTypeNumber("mi1").serialNum("ser1").build(),
                    MeasurementInstrument.builder().miTypeNumber("mi2").serialNum("ser2").build()
            ));

            Mean mean = MeanFactory.createMeans(vrfRecord);

            assertNotNull(mean.getMis());
            assertEquals(2, mean.getMis().getMiLIst().size());
            assertTrue(mean.getMis().getMiLIst().stream()
                    .anyMatch(mi -> mi.getTypeNum().equals("mi1") && mi.getManufactureNum().equals("ser1")));
            assertTrue(mean.getMis().getMiLIst().stream()
                    .anyMatch(mi -> mi.getTypeNum().equals("mi2") && mi.getManufactureNum().equals("ser2")));
        }
    }
}