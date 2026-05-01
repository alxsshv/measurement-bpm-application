package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.MiInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MiInfoFactory {

    public static MiInfo createMiInfo(ArshinVerificationRecord vrfRecord){
        MiInfo miInfo = new MiInfo();
        miInfo.setSingleMi(SingleMiFactory.createSingleMi(vrfRecord));
        return miInfo;
    }
}
