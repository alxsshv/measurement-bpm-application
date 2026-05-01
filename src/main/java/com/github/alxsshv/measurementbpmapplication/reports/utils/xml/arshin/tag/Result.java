package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {
    @XmlElement(name = "miInfo")
    private MiInfo miInfo;
    @XmlElement(name = "signCipher")
    private String signCipher;
    @XmlElement(name = "miOwner")
    private String miOwner;
    @XmlElement(name = "vrfDate")
    private String vrfDate;
    @XmlElement(name = "validDate")
    private String validDate;
    @XmlElement(name = "type")
    private int vriType;
    @XmlElement(name = "calibration")
    private boolean calibration;
    @XmlElement(name = "applicable")
    private Applicable applicable;
    @XmlElement(name = "inapplicable")
    private Inapplicable inapplicable;
    @XmlElement(name = "docTitle")
    private String docTitle;
    @XmlElement(name = "metrologist")
    private String metrologist;
    @XmlElement(name = "means")
    private Mean means;
    @XmlElement(name = "conditions")
    private Conditions conditions;
    @XmlElement(name = "brief_procedure")
    private BriefProcedure briefProcedure;
}
