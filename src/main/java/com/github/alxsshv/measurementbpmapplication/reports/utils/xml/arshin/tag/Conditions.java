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
@XmlRootElement(name = "conditions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Conditions {
    @XmlElement(name = "temperature")
    private String temperature;
    @XmlElement(name = "pressure")
    private String pressure;
    @XmlElement(name = "hymidity")
    private String humidity;
    @XmlElement(name = "other")
    private String other;
}
