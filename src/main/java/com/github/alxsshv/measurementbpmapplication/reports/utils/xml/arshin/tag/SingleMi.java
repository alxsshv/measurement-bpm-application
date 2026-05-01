package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "singleMI")
@XmlAccessorType(XmlAccessType.FIELD)
public class SingleMi extends VerificationObject{
    @XmlElement(name = "mitypeNumber")
    private String miTypeNumber;
    @XmlElement(name = "manufactureNum")
    private String manufactureNum;
    @XmlTransient
    private String inventoryNum;
    @XmlTransient
    private int manufactureYear;
    @XmlElement(name = "modification")
    private String modification;
}
