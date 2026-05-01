package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag;

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
@XmlRootElement(name = "ApprovedEmployee")
@XmlAccessorType(XmlAccessType.NONE)
public class ApprovedEmployee {
    @XmlElement(name = "Name")
    private EmployeeName employeeName;
    @XmlElement(name = "SNILS")
    private String snils;
}
