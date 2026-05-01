package com.github.alxsshv.measurementbpmapplication.common;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.time.temporal.ChronoField.*;

public class CyrillicDateTimeFormatter {

    private CyrillicDateTimeFormatter() {
    }

    public static DateTimeFormatter getFormatter() {
        Map<Long, String> dow = new HashMap<>();
        dow.put(1L, "Пн");
        dow.put(2L, "Вт");
        dow.put(3L, "Ср");
        dow.put(4L, "Чт");
        dow.put(5L, "Пт");
        dow.put(6L, "Сб");
        dow.put(7L, "Вс");
        Map<Long, String> moy = new HashMap<>();
        moy.put(1L, "Января");
        moy.put(2L, "Февраля");
        moy.put(3L, "Марта");
        moy.put(4L, "Апреля");
        moy.put(5L, "Мая");
        moy.put(6L, "Июня");
        moy.put(7L, "Июля");
        moy.put(8L, "Августа");
        moy.put(9L, "Сентября");
        moy.put(10L, "Октября");
        moy.put(11L, "Ноября");
        moy.put(12L, "Декабря");

        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .parseLenient()
                .optionalStart()
                .appendText(DAY_OF_WEEK, dow)
                .appendLiteral(", ")
                .optionalEnd()
                .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(' ')
                .appendText(MONTH_OF_YEAR, moy)
                .appendLiteral(' ')
                .appendValue(YEAR, 4)  // 2 digit year not handled
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalEnd()
                .appendLiteral(' ')
                .appendOffset("+HHMM", "")  // should handle UT/Z/EST/EDT/CST/CDT/MST/MDT/PST/MDT
                .toFormatter();
    }
}

