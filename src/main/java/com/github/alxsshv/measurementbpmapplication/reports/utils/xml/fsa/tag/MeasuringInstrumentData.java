package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@NoArgsConstructor
@XmlRootElement(name = "VerificationMeasuringInstrumentData")
public class MeasuringInstrumentData {
    @XmlElement(name = "VerificationMeasuringInstrument")
    private ArrayList<MeasurementInstrument> instruments  = new ArrayList<>();

    public void add(MeasurementInstrument instrument){
        instruments.add(instrument);
    }

    @Override
    public String toString() {
        return "MeasuringInstrumentDataDto{" +
                "instruments=" + instruments +
                '}';
    }
}
