package com.github.alxsshv.measurementbpmapplication.reportscheduler.controller;

import com.github.alxsshv.measurementbpmapplication.common.dto.reportscheduler.FsaReportTaskDto;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.mapper.FsaReportTaskMapper;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.IllegalExcelFileException;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.service.FsaReportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("scheduled/report")
@RequiredArgsConstructor
public class ReportsBuildingController {

    private final FsaReportTaskService fsaReportTaskService;

    @PostMapping("/fsa")
    public void buildFsaReport(@RequestBody MultipartFile file) {
        if (file == null) {
            throw new IllegalExcelFileException("Файл для формирования отчета не выбран или пуст");
        }
        fsaReportTaskService.create(file);
    }

    @GetMapping("/fsa")
    public List<FsaReportTaskDto> getTasks() {
        return FsaReportTaskMapper.toDtoList(fsaReportTaskService.getAll());
    }

    @DeleteMapping("/fsa/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getTasks(@PathVariable("id") String id) {
        fsaReportTaskService.deleteById(id);
    }

}
