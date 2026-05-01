package com.github.alxsshv.measurementbpmapplication.reportscheduler.repository;

import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;

import java.util.List;
import java.util.Optional;

public interface FsaReportTaskRepository {

    Optional<FsaReportTask> findById(String id);
    List<FsaReportTask> findAll();
    void delete(String id);
    FsaReportTask save(FsaReportTask task);
    void update(FsaReportTask task);
    void deleteAll();
}
