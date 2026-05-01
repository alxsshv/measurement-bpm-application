package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "VerificationMeasuringInstrument")
public class MeasurementInstrument {
    @XmlElement(name = "NumberVerification")
    private String numberVerification;
    @XmlElement(name = "DateVerification")
    private String dateVerification;
    @XmlElement(name = "DateEndVerification", nillable = true)
    private String dateEndVerification;
    @XmlElement(name = "TypeMeasuringInstrument")
    private String typeMeasuringInstrument;
    @XmlElement(name = "ApprovedEmployees")
    private ApprovedEmployee approvedEmployee;
    @XmlElement(name = "ResultVerification")
    private int resultVerification;

    @Override
    public String toString() {
        return "VerificationMeasurementInstrument{" +
                "numberVerification='" + numberVerification + '\'' +
                ", dateVerification='" + dateVerification + '\'' +
                ", dateEndVerification='" + dateEndVerification + '\'' +
                ", typeMeasuringInstrument='" + typeMeasuringInstrument + '\'' +
                ", approvedEmpolyee='" + approvedEmployee + '\'' +
                ", resultVerification=" + resultVerification +
                '}';
    }
}
