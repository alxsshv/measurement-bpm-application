package com.github.alxsshv.measurementbpmapplication.filestorage.utils;

import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileStorgePathSelector {

    @Value("${logging.file.path}")
    private String loggingPath;

    private final PathsConfig paths;

    public String getFileStoragePath(FileType type) {
        return switch (type) {
            case FSA_VERIFICATION_REPORT -> paths.fsaReportStoragePath();
            case ARSHIN_VERIFICATION_REPORT -> paths.arshinReportStoragePath();
            case LOGS -> loggingPath;
            case TEMPORARY -> paths.temporary();
        };
    }


}
