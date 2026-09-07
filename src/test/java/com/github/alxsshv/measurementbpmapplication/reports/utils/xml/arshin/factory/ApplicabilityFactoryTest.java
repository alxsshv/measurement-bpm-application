package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Applicable;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Inapplicable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicabilityFactoryTest {

    @Nested
    class TestCreateApplicableMethod {

        @Test
        @DisplayName("Заполняет номер наклейки, штампы паспорта и СИ")
        void createsApplicableWithStamps() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setStickerNum("001");
            vrfRecord.setSignPass(true);
            vrfRecord.setSignMi(false);

            Applicable applicable = ApplicabilityFactory.createApplicable(vrfRecord);

            assertEquals("001", applicable.getStickerNum());
            assertTrue(applicable.isSignPass());
            assertFalse(applicable.isSignMi());
        }

        @Test
        @DisplayName("Подставляет \"отсутствует\", если номер наклейки не указан")
        void setsAbsentStickerWhenNull() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();

            Applicable applicable = ApplicabilityFactory.createApplicable(vrfRecord);

            assertEquals("отсутствует", applicable.getStickerNum());
        }
    }

    @Nested
    class TestCreateInapplicableMethod {

        @Test
        @DisplayName("Заполняет причину непригодности")
        void createsInapplicableWithReasons() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setInapplicableReason("Не соответствует классу");

            Inapplicable inapplicable = ApplicabilityFactory.createInapplicable(vrfRecord);

            assertEquals("Не соответствует классу", inapplicable.getReasons());
        }

        @Test
        @DisplayName("Сохраняет null причину")
        void keepsNullReasons() {
            ArshinVerificationRecord vrfRecord = new ArshinVerificationRecord();
            vrfRecord.setInapplicableReason(null);

            Inapplicable inapplicable = ApplicabilityFactory.createInapplicable(vrfRecord);

            assertNull(inapplicable.getReasons());
        }
    }
}