package com.github.alxsshv.measurementbpmapplication.reports.service.arshin;

import com.github.alxsshv.measurementbpmapplication.common.config.OrganizationConfig;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.ArshinXmlWriter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory.VerificationApplicationFactory;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.VerificationApplication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ArshinXmlService {

    private final ArshinXmlWriter arshinXmlWriter;

    private final OrganizationConfig organizationConfig;

    public Resource createXml(List<ArshinVerificationRecord> records) {
        VerificationApplication application = VerificationApplicationFactory.createApplication(records, organizationConfig.sign());
        return arshinXmlWriter.writeXmlToResource(application);
    }


}



