package com.github.alxsshv.measurementbpmapplication.reports.utils.excel;

import com.github.alxsshv.measurementbpmapplication.reports.entity.MeasRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import jakarta.validation.Valid;

import java.util.List;

public interface ExcelParser {

    List<? extends MeasRecord> parse(@Valid List<ExcelDataObject> excelObjects);
}
