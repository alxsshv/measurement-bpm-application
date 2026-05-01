package com.github.alxsshv.measurementbpmapplication.reportscheduler.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FsaReportTask {

    private String id;
    private String title;
    private String contentFilename;
    private String reportFilename;
    private Status status;
    private LocalDateTime creationTimestamp;
    private LocalDateTime lastAttemptTimeStamp;

    public enum Status {
        CREATED,
        PROCESSED,
        FAILED,
        COMPLETED_SUCCESSFULLY,
        COMPLETED_UNSUCCESSFULLY
    }




}
