package com.github.alxsshv.measurementbpmapplication.reports.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    @Mock
    private FileStorageApi fileStorageApi;

    @Nested
    class TestUploadMethod {

        @Test
        @DisplayName("Передаёт файл в хранилище")
        void uploadsFileToStorage() {
            FileUploadService fileUploadService = new FileUploadService(fileStorageApi);
            FileDto fileDto = new FileDto("report.xml", "content".getBytes(), FileType.ARSHIN_VERIFICATION_REPORT);

            fileUploadService.upload(fileDto);

            verify(fileStorageApi).upload(fileDto);
        }
    }
}