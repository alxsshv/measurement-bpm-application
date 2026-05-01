package com.github.alxsshv.measurementbpmapplication.common.dto.reportscheduler;

public record FsaReportTaskDto (

    String id,
    String title,
    String reportFilename,
    String status,
    String creationTimestamp,
    String lastAttemptTimeStamp

) {}
