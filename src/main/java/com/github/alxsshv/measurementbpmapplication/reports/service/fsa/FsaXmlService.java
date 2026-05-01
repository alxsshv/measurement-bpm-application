package com.github.alxsshv.measurementbpmapplication.reports.service.fsa;

import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.ArshinXmlWriter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.FsaXmlWriter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory.FsaReportMessageFactory;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.FsaReportMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class FsaXmlService {

    private final FsaXmlWriter fsaXmlWriter;

    private final ArshinXmlWriter arshinXmlWriter;

    public Resource createXml(List<FsaVerificationRecord> records) {
        FsaReportMessage fsaMessage = FsaReportMessageFactory.createMessage(records);
        return fsaXmlWriter.writeXmlToResource(fsaMessage);
    }



}



