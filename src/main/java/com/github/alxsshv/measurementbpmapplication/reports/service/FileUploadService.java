package com.github.alxsshv.measurementbpmapplication.reports.service;

import com.github.alxsshv.measurementbpmapplication.common.dto.filestorage.FileDto;
import com.github.alxsshv.measurementbpmapplication.filestorage.api.FileStorageApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileStorageApi fileStorageApi;

    public void upload(FileDto fileDto) {
        fileStorageApi.upload(fileDto);
    }

}
