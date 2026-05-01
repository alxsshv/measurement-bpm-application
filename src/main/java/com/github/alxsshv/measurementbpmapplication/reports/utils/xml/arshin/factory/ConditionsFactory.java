package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;


import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Conditions;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConditionsFactory {

    public static Conditions createConditions(ArshinVerificationRecord vrfRecord){
        Conditions conditions = new Conditions();
        conditions.setHumidity(vrfRecord.getHumidity());
        conditions.setPressure(vrfRecord.getPressure());
        conditions.setTemperature(vrfRecord.getTemperature());
        return conditions;
    }
}
