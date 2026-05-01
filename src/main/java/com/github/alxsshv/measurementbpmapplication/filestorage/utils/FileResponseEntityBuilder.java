package com.github.alxsshv.measurementbpmapplication.filestorage.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class FileResponseEntityBuilder {

    /** Собирает ResponseEntity для выгрузки файла */
    public static ResponseEntity<Resource> buildFileResponseEntity(Resource resource, String filename) {
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/xml"));
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
