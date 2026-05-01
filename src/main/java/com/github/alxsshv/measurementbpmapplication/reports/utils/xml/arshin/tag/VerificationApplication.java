package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;


import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@XmlRootElement(name = "application")
@XmlAccessorType(XmlAccessType.FIELD)
public class VerificationApplication {
    @XmlElements({
            @XmlElement(name = "result", type = Result.class)
    })
    private List<Result> results = new ArrayList<>();
}
