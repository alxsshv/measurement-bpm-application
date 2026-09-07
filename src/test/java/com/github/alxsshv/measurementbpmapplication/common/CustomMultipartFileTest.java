package com.github.alxsshv.measurementbpmapplication.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomMultipartFileTest {

    private final byte[] content = "file-content".getBytes(StandardCharsets.UTF_8);

    private CustomMultipartFile createFile(byte[] bytes) {
        return CustomMultipartFile.builder()
                .name("file")
                .originalFilename("report.xml")
                .contentType("application/xml")
                .bytes(bytes)
                .build();
    }

    @Nested
    class TestGetNameMethod {

        @Test
        @DisplayName("Возвращает имя поля")
        void returnsName() {
            CustomMultipartFile file = createFile(content);

            assertEquals("file", file.getName());
        }
    }

    @Nested
    class TestGetOriginalFilenameMethod {

        @Test
        @DisplayName("Возвращает оригинальное имя файла")
        void returnsOriginalFilename() {
            CustomMultipartFile file = createFile(content);

            assertEquals("report.xml", file.getOriginalFilename());
        }
    }

    @Nested
    class TestGetContentTypeMethod {

        @Test
        @DisplayName("Возвращает тип контента")
        void returnsContentType() {
            CustomMultipartFile file = createFile(content);

            assertEquals("application/xml", file.getContentType());
        }
    }

    @Nested
    class TestIsEmptyMethod {

        @Test
        @DisplayName("Возвращает false для файла с содержимым")
        void returnsFalseForNonEmptyFile() {
            CustomMultipartFile file = createFile(content);

            assertFalse(file.isEmpty());
        }

        @Test
        @DisplayName("Возвращает true для пустого массива байтов")
        void returnsTrueForEmptyBytes() {
            CustomMultipartFile file = createFile(new byte[0]);

            assertTrue(file.isEmpty());
        }

        @Test
        @DisplayName("Возвращает true, когда байты равны null")
        void returnsTrueForNullBytes() {
            CustomMultipartFile file = createFile(null);

            assertTrue(file.isEmpty());
        }
    }

    @Nested
    class TestGetSizeMethod {

        @Test
        @DisplayName("Возвращает размер файла")
        void returnsSize() {
            CustomMultipartFile file = createFile(content);

            assertEquals(content.length, file.getSize());
        }

        @Test
        @DisplayName("Бросает исключение, когда байты равны null")
        void throwsWhenBytesAreNull() {
            CustomMultipartFile file = createFile(null);

            assertThrows(NullPointerException.class, file::getSize);
        }
    }

    @Nested
    class TestGetBytesMethod {

        @Test
        @DisplayName("Возвращает содержимое файла")
        void returnsBytes() {
            CustomMultipartFile file = createFile(content);

            assertArrayEquals(content, file.getBytes());
        }

    }

    @Nested
    class TestGetInputStreamMethod {

        @Test
        @DisplayName("Возвращает поток с содержимым файла")
        void returnsInputStreamWithContent() throws IOException {
            CustomMultipartFile file = createFile(content);

            try (InputStream inputStream = file.getInputStream()) {
                assertArrayEquals(content, inputStream.readAllBytes());
            }
        }

        @Test
        @DisplayName("Бросает исключение, когда байты равны null")
        void throwsWhenBytesAreNull() {
            CustomMultipartFile file = createFile(null);

            assertThrows(NullPointerException.class, file::getInputStream);
        }
    }

    @Nested
    class TestTransferToMethod {

        @Test
        @DisplayName("Записывает содержимое файла в указанный файл")
        void transfersContentToDestination(@TempDir Path tempDir) throws IOException {
            CustomMultipartFile file = createFile(content);
            Path destination = tempDir.resolve("out.xml");

            file.transferTo(destination.toFile());

            assertArrayEquals(content, Files.readAllBytes(destination));
        }

        @Test
        @DisplayName("Бросает исключение, когда байты равны null")
        @SuppressWarnings("java:S5778")
        void throwsWhenBytesAreNull(@TempDir Path tempDir) {
            CustomMultipartFile file = createFile(null);
            Path destination = tempDir.resolve("out.xml");

            assertThrows(NullPointerException.class, () -> file.transferTo(destination.toFile()));
        }
    }
}