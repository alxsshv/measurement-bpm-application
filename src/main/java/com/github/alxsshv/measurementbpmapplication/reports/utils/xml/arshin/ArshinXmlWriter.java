package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin;

import com.github.alxsshv.measurementbpmapplication.reports.exception.ReportCreationException;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.VerificationApplication;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class ArshinXmlWriter {

    public Resource writeXmlToResource(VerificationApplication application) {
        try{
            JAXBContext context = JAXBContext.newInstance(VerificationApplication.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(application,outputStream);
            log.info("xml файл сформирован");
            return new ByteArrayResource(outputStream.toByteArray()) ;
        } catch (JAXBException ex) {
            log.error("Jaxb-context ошибочен: {}", ex.getMessage());
            throw new ReportCreationException("Ошибка формирования xml файла");
        }
    }


}
