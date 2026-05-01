package com.github.alxsshv.measurementbpmapplication.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomMultipartFile implements MultipartFile {
    private String name;
    private String originalFilename;
    private String contentType;
    private byte[] bytes;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    @Override
    public long getSize() {
        return this.bytes.length;
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public InputStream getInputStream()  {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Path path = Paths.get(dest.getPath());
        Files.write(path, bytes);
    }
}
