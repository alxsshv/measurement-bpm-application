package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;

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
@XmlRootElement(name = "applicable")
@XmlAccessorType(XmlAccessType.FIELD)
public class Applicable extends Applicability {
    @XmlElement(name = "stickerNum")
    private String stickerNum;
    @XmlElement(name = "signPass")
    private boolean signPass;
    @XmlElement(name = "signMi")
    private boolean signMi;
}
