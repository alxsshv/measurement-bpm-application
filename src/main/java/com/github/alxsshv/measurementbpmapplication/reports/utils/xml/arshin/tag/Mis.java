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
@XmlRootElement(name = "mis")
@XmlAccessorType(XmlAccessType.FIELD)
//Средства измерений, применяемые при поверке
public class Mis {
    @XmlElements({
            @XmlElement(name = "mi", type = Mi.class)
    })
    private List<Mi> miLIst = new ArrayList<>();
}
