package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="Message")
public class FsaReportMessage {
    @XmlElement(name="VerificationMeasuringInstrumentData")
    private MeasuringInstrumentData data;
    @XmlElement(name="SaveMethod")
    private int saveMethod;

    @Override
    public String toString() {
        return "Message{" +
                "data=" + data +
                ", saveMethod=" + saveMethod +
                '}';
    }
}
