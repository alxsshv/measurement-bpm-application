package com.github.alxsshv.measurementbpmapplication.employee.utils;

import com.github.alxsshv.measurementbpmapplication.employee.exception.EmployeeStorageException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
@NoArgsConstructor
public class PathResolver {

    public void createFilePathIfNotExist(String path) throws IOException {
        if (!StringUtils.hasText(path)) {
            throw new EmployeeStorageException("Путь к файлу для хранения сведений о поверителе не указан");
        }
        Path filePath = Path.of(path);
        Path directory = filePath.getParent();
        if (directory != null) {
            Files.createDirectories(directory);
        }
    }

}
