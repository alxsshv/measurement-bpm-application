package com.github.alxsshv.measurementbpmapplication.common.config;

import java.time.ZoneId;

public class AppConstants {

    private AppConstants() {
    }
    public static  final String TIME_ZONE = "Europe/Moscow";
    public static final ZoneId TIME_ZONE_ID = ZoneId.of(TIME_ZONE);
}
