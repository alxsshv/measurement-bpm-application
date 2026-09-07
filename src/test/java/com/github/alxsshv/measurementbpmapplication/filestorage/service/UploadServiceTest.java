package com.github.alxsshv.measurementbpmapplication.filestorage.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileType;
import com.github.alxsshv.measurementbpmapplication.filestorage.utils.FileStorgePathSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    @Mock
    private FileStorgePathSelector pathSelector;

    @TempDir
    private static Path tempDir;

    private UploadService uploadService;

    @BeforeEach
    void setUp() {
        uploadService = new UploadService(pathSelector);
    }

    @Nested
    class TestUploadMethod {

        @Test
        @DisplayName("Сохраняет файл в хранилище по пути выбранного типа")
        void uploadWritesFileToStoragePath() throws IOException {
            when(pathSelector.getFileStoragePath(FileType.FSA_VERIFICATION_REPORT)).thenReturn(tempDir.toString());
            FileDto fileDto = new FileDto("report.xml", "content".getBytes(), FileType.FSA_VERIFICATION_REPORT);

            uploadService.upload(fileDto);

            assertArrayEquals("content".getBytes(), Files.readAllBytes(tempDir.resolve("report.xml")));
        }

        @Test
        @DisplayName("Создаёт директорию хранения, если она отсутствует")
        void uploadCreatesMissingDirectory() {
            Path nestedDir = tempDir.resolve("nested");
            when(pathSelector.getFileStoragePath(FileType.TEMPORARY)).thenReturn(nestedDir.toString());
            FileDto fileDto = new FileDto("file.tmp", "data".getBytes(), FileType.TEMPORARY);

            uploadService.upload(fileDto);

            assertTrue(Files.exists(nestedDir.resolve("file.tmp")));
        }

        @Test
        @DisplayName("Ничего не делает при передаче null")
        void uploadDoesNothingForNullFile() {
            uploadService.upload(null);

            verify(pathSelector, never()).getFileStoragePath(any());
        }
    }
}