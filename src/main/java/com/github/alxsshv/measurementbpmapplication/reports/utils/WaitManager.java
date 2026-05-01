package com.github.alxsshv.measurementbpmapplication.reports.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class WaitManager {

    public static void waitMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            log.info("Поток был прерван по время ожидания: {}", ex.getMessage());
        }
    }

}
