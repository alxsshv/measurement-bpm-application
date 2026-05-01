package com.github.alxsshv.measurementbpmapplication.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "paths")
public record PathsConfig (
        String fsaReportStoragePath,
        String arshinReportStoragePath,
        String arshinVriUri,
        String employeeStoragePath,
        String fsaReportTaskStoragePath,
        String temporary
) {}
