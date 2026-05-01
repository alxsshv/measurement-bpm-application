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
@XmlRootElement(name = "miInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class MiInfo {
    @XmlElement(name = "singleMI")
    private SingleMi singleMi;
    private PartyMi partyMi;
    private EtaMi etaMi;
}
