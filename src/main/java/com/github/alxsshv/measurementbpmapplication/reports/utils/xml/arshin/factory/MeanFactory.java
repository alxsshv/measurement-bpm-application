package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MiStandard;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Mean;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.MiEta;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MeanFactory {

    public static Mean createMeans(ArshinVerificationRecord vrfRecord){
        Mean mean = new Mean();
        mean.getMiEtaList().addAll(buildMietaList(vrfRecord));
        if (!vrfRecord.getVerificationMis().isEmpty()) {
            mean.setMis(MisFactory.createMis(vrfRecord));
        }
        return mean;
    }

    private static List<MiEta> buildMietaList(ArshinVerificationRecord vrfRecord){
        List<MiEta> miEtaList = new ArrayList<>();
        for (MiStandard standard : vrfRecord.getMiStandards()){
            MiEta miEta = new MiEta();
            miEta.setNumber(standard.getArshinNumber());
            miEtaList.add(miEta);
        }
        return miEtaList;
    }
}
