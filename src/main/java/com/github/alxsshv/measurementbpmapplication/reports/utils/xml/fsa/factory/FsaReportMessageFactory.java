package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory;


import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.FsaReportMessage;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.MeasurementInstrument;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.MeasuringInstrumentData;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FsaReportMessageFactory {

    public static FsaReportMessage createMessage(List<FsaVerificationRecord> records){
        MeasuringInstrumentData data = new MeasuringInstrumentData();
        for (FsaVerificationRecord vrfRecord: records) {
            MeasurementInstrument instrument = MeasurementInstrumentFactory.createInstrument(vrfRecord);
            data.add(instrument);
        }
        return new FsaReportMessage(data,2);
    }
}
