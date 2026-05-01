package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.SingleMi;
import lombok.experimental.UtilityClass;


//TODO: реализовать возможность поверки си применяемого в качестве эталона и партии си и
// реализовать в данном классе методы createEtaMI и createPartyMi и селектор

@UtilityClass
public class SingleMiFactory {

    public static SingleMi createSingleMi(ArshinVerificationRecord vrfRecord) {
        SingleMi singleMi = new SingleMi();
        singleMi.setMiTypeNumber(vrfRecord.getMi().getMiTypeNumber());
        singleMi.setManufactureNum(vrfRecord.getMi().getSerialNum());
        singleMi.setModification(vrfRecord.getMi().getModification());
        return singleMi;
    }


}
