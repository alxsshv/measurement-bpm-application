package com.github.alxsshv.measurementbpmapplication.employee.utils;

import com.github.alxsshv.measurementbpmapplication.employee.utils.PathResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class PathResolverTest {
    private final PathResolver pathResolver = new PathResolver();

    @Test
    void testCreateFilePathIfNotExist(@TempDir Path tempDir) throws IOException {
        pathResolver.createFilePathIfNotExist(tempDir + "/nextFolder/file.txt");
        Assertions.assertTrue(Files.exists(Path.of(tempDir + "/nextFolder/")));
    }


}
