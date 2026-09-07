package com.github.alxsshv.measurementbpmapplication.reports.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WaitManagerTest {

    @Nested
    class TestWaitMillisMethod {

        @Test
        @DisplayName("Приостанавливает выполнение потока на указанное количество миллисекунд")
        void waitsAtLeastSpecifiedMillis() {
            long start = System.nanoTime();

            WaitManager.waitMillis(10);

            long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
            assertTrue(elapsedMillis >= 10, "Поток не ждал достаточное время");
        }

        @Test
        @DisplayName("Не бросает исключение при прерванном потоке")
        void doesNotThrowWhenThreadInterrupted() {
            Thread.currentThread().interrupt();

            assertDoesNotThrow(() -> WaitManager.waitMillis(1));
        }
    }
}