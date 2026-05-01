package com.github.alxsshv.measurementbpmapplication.reports.service.arshin;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationReportDto;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.mapper.ArshinVerificationRecordMapper;
import com.github.alxsshv.measurementbpmapplication.reports.service.FileUploadService;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.ExcelArshinVerificationRecordParser;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.ExcelMultipartFileReader;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.github.alxsshv.measurementbpmapplication.reports.utils.xml.XmlContentUtils.xmlContentAsBytes;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArshinReportFacade {

    private final ExcelMultipartFileReader excelReader;

    private final ExcelArshinVerificationRecordParser excelParser;

    private final ArshinXmlService xmlService;

    private final FileUploadService fileUploadService;

    private final FileStorageApi fileStorageApi;


    public ArshinVerificationReportDto buildReport(MultipartFile file) {
        List<ExcelDataObject> excelObjects = excelReader.getDataFromExcelFile(file);
        List<ArshinVerificationRecord> records = excelParser.parse(excelObjects);
        String filename = fileStorageApi.generateFilename(FileType.ARSHIN_VERIFICATION_REPORT);
        Resource xmlContent = xmlService.createXml(records);
        fileUploadService.upload(new FileDto(filename, xmlContentAsBytes(xmlContent), FileType.ARSHIN_VERIFICATION_REPORT));
        return new ArshinVerificationReportDto (filename, ArshinVerificationRecordMapper.map(records));
    }



}
