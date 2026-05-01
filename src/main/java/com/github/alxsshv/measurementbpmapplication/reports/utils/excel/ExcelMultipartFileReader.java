package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;

import com.github.alxsshv.measurementbpmapplication.reports.exception.ExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Slf4j
@Component
public class ExcelMultipartFileReader {

    public List<ExcelDataObject> getDataFromExcelFile(MultipartFile file) {
        log.info("Читаем данные из файла");
        try {
            InputStream is = new BufferedInputStream(file.getInputStream());
            return Poiji.fromExcel(is, PoijiExcelType.XLSX, ExcelDataObject.class);
        } catch (IOException ex) {
            String errorMessage = String.format("Ошибка чтения данных из excel файла [ %s ]", file.getOriginalFilename());
            log.error("{}: {}", errorMessage, ex.getMessage());
            throw new ExcelFileException(errorMessage);
        }
    }
}
