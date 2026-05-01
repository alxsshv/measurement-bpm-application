package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@XmlRootElement(name = "Name")
@XmlAccessorType(XmlAccessType.NONE)
public class EmployeeName {
    @XmlElement(name = "Last")
    private String surname;
    @XmlElement(name = "First")
    private String name;
    private String patronymic;


    @Override
    public String toString() {
        return "EmployeeName{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}