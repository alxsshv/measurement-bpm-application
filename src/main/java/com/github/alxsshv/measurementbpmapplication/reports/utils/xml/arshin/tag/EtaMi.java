package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class EtaMi extends VerificationObject{
    private String regNumber;
    private String mitypeNumber;
    private String mitypeUrl;
    private String mitypeTitle;
    private String mitypeType;
    private String modification;
    private String manufactureNum;
    private int manufactureYear;
    private String rankCode;
    private String rankTitle;
    private String schemaTitle;
}
