package com.github.alxsshv.measurementbpmapplication.filestorage.service;

import com.github.alxsshv.measurementbpmapplication.common.config.AppConstants;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FilenameGenerationService {

    private static final String ARSHIN_VERIFICATION_REPORT_PREFIX = "ArshinReport";
    private static final String FSA_VERIFICATION_REPORT_PREFIX = "FsaReport";

    public String generateFilename(FileType fileType) {
        return switch (fileType) {
            case ARSHIN_VERIFICATION_REPORT ->
                    ARSHIN_VERIFICATION_REPORT_PREFIX + UUID.randomUUID() + FileType.ARSHIN_VERIFICATION_REPORT.getExtension();
            case FSA_VERIFICATION_REPORT -> FSA_VERIFICATION_REPORT_PREFIX + UUID.randomUUID() + FileType.FSA_VERIFICATION_REPORT.getExtension();
            case LOGS -> "spring.log";
            case TEMPORARY -> UUID.randomUUID() + "_" + LocalDateTime.now(AppConstants.TIME_ZONE_ID) + FileType.TEMPORARY.getExtension();
        };
    }
}
