package com.github.alxsshv.measurementbpmapplication.reports.service.fsa;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin.ArshinVerificationReportDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.reports.fsa.FsaReportDto;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.mapper.ArshinVerificationRecordMapper;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.mapper.FsaVerificationRecordDtoMapper;
import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.service.FileUploadService;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.ExcelArshinVerificationRecordParser;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.ExcelFsaVerificationRecordParser;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.ExcelMultipartFileReader;
import com.github.alxsshv.measurementbpmapplication.reports.utils.excel.entity.ExcelDataObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.github.alxsshv.measurementbpmapplication.reports.utils.xml.XmlContentUtils.xmlContentAsBytes;

@Slf4j
@Service
@RequiredArgsConstructor
public class FsaReportFacade {

    private final ExcelMultipartFileReader excelReader;
    private final ExcelFsaVerificationRecordParser verificationRecordParser;
    private final VerificationRecordSupplementService recordService;
    private final FsaXmlService xmlService;
    private final FileStorageApi fileStorageApi;
    private final FileUploadService fileUploadService;

    public FsaReportDto buildReport(MultipartFile file) {
        List<ExcelDataObject> objects = excelReader.getDataFromExcelFile(file);
        List<FsaVerificationRecord> records = verificationRecordParser.parse(objects);
        List<FsaVerificationRecord> updatedRecords = recordService.updateRecordsFromArshin(records);
        String filename = fileStorageApi.generateFilename(FileType.FSA_VERIFICATION_REPORT);
        Resource xmlContent = xmlService.createXml(records);
        fileUploadService.upload(new FileDto(filename, xmlContentAsBytes(xmlContent), FileType.FSA_VERIFICATION_REPORT));
        return new FsaReportDto(filename, FsaVerificationRecordDtoMapper.map(updatedRecords));
    }

}
