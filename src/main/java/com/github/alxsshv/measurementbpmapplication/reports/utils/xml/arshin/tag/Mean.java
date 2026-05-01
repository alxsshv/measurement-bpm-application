package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "means")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mean {
    private List<Uve> uveList = new ArrayList<>();
    private List <Ses> sesList = new ArrayList<>();
    @XmlElements({
            @XmlElement(name = "mieta", type = MiEta.class)
    })
    private List<MiEta> miEtaList = new ArrayList<>();
    @XmlElement(name = "mis")
    private Mis mis;
    private List <Reagent> reagentList = new ArrayList<>();
    private String oMethod;
}
