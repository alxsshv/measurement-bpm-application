package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Result;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.VerificationApplication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.List;

@UtilityClass
public class VerificationApplicationFactory {

    public static VerificationApplication createApplication(List<ArshinVerificationRecord> records,  String signCipher){
        VerificationApplication application = new VerificationApplication();
        for (ArshinVerificationRecord verificationRecord: records) {
            Result result = ResultFactory.createResult(verificationRecord, signCipher);
            application.getResults().add(result);
        }
        return application;
    }


}
