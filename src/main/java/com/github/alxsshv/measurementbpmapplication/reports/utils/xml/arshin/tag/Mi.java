package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "mi")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mi {
    @XmlElement(name = "typeNum")
    private String typeNum;
    @XmlElement(name = "manufactureNum")
    private String manufactureNum;
}
