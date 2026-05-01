package com.github.alxsshv.measurementbpmapplication.common.dto.filestorage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Objects;

public record FileDto (

        @NotBlank(message = "Имя файла не указано или пусто")
        String filename,

        @NotNull(message = "Файл не содержит данных")
        byte[] content,

        @NotNull(message = "Не указан тип файла")
        FileType fileType
) {
        @Override
        public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                FileDto fileDto = (FileDto) o;
                return Objects.deepEquals(content, fileDto.content) && Objects.equals(filename, fileDto.filename) && fileType == fileDto.fileType;
        }

        @Override
        public int hashCode() {
                return Objects.hash(filename, Arrays.hashCode(content), fileType);
        }

        @Override
        public String toString() {
                return "FileDto{" +
                        "filename='" +  filename + '\'' +
                        ", fileType=" + fileType +
                        '}';
        }
}
