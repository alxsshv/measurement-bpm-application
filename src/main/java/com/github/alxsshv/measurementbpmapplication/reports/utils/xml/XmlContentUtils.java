package com.github.alxsshv.measurementbpmapplication.reports.utils.xml;

import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportCreationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;

@UtilityClass
@Slf4j
public class XmlContentUtils {

    public static byte[] xmlContentAsBytes(Resource xmlContent) {
        try {
            return xmlContent.getContentAsByteArray();
        } catch (IOException ex) {
            log.error("Ошибка преобразования xml контента в массив байт: {}", ex.getMessage());
            throw new ReportCreationException("При попытке формирования xml файла возникла ошибка");
        }
    }

}
