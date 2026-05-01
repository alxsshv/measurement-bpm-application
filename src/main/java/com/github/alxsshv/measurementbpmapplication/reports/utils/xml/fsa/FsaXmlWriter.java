package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa;

import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportCreationException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.FsaReportMessage;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FsaXmlWriter {

    public Resource writeXmlToResource(FsaReportMessage message) {
        try{
            JAXBContext context = JAXBContext.newInstance(FsaReportMessage.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "schema.xsd");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(message, outputStream);
            log.info("xml файл сформирован");
            return new ByteArrayResource(outputStream.toByteArray()) ;
        } catch (JAXBException ex) {
            log.error("Jaxb-context ошибочен: {}", ex.getMessage());
            throw new ReportCreationException("Ошибка формирования xml файла");
        }
    }


}