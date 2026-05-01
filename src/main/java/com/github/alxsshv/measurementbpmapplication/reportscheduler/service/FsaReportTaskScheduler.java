package com.github.alxsshv.measurementbpmapplication.reportscheduler.service;


import com.github.alxsshv.measurementbpmapplication.common.config.AppConstants;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FsaReportTaskScheduler {
    private static final int TASK_TTL_IN_HOURS = 48;

    private final FsaReportTaskExecutor taskExecutor;
    private final FsaReportTaskService fsaReportTaskService;

    @Scheduled(fixedDelay = 120_000)
    public void executeTasks() {
        List<FsaReportTask> tasks = fsaReportTaskService.getAll();
        tasks.forEach(taskExecutor::execute);
    }

    /** Метод запускает удаление истекших задач каждый день в 7:58 и 17:58 */
    @Scheduled(cron = "0 58 7,17 * * ?", zone = AppConstants.TIME_ZONE)
    @Async
    public void deleteOverdueTasks() {
        List<FsaReportTask> tasks = fsaReportTaskService.getAll();
        for (FsaReportTask task : tasks) {
            if (task.getCreationTimestamp().plusHours(TASK_TTL_IN_HOURS).isBefore(LocalDateTime.now(AppConstants.TIME_ZONE_ID))) {
                log.info("Удаление просроченой задачи [ {} ]", task.getTitle());
                fsaReportTaskService.delete(task);
            }
        }
    }


}
