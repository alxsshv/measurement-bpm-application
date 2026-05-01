package com.github.alxsshv.measurementbpmapplication.common.dto.filestorage;

import lombok.Getter;

@Getter
public enum FileType {
    FSA_VERIFICATION_REPORT(".xml"),
    ARSHIN_VERIFICATION_REPORT(".xml"),
    LOGS(".log"),
    TEMPORARY(".tmp");

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

}
