package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Mi;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Mis;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MisFactory {

    public static Mis createMis(ArshinVerificationRecord vrfRecord){
        Mis mis = new Mis();
        for (MeasurementInstrument verificationMi : vrfRecord.getVerificationMis()){
            Mi mi = new Mi();
            mi.setTypeNum(verificationMi.getMiTypeNumber());
            mi.setManufactureNum(verificationMi.getSerialNum());
            mis.getMiLIst().add(mi);
        }
        return mis;
    }
}
