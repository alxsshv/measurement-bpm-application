package com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class VriItem {
    @JsonProperty("vri_id")
    private String vriId;
    @JsonProperty("org_title")
    private String orgTitle;
    @JsonProperty("mit_number")
    private String mitNumber;
    @JsonProperty("mit_title")
    private String mitTitle;
    @JsonProperty("mit_notation")
    private String mitNotation;
    @JsonProperty("mi_modification")
    private String miModification;
    @JsonProperty("mi_number")
    private String miNumber;
    @JsonProperty("verification_date")
    private String verificationDate;
    @JsonProperty("valid_date")
    private String validDate;
    @JsonProperty("result_docnum")
    private String resultDocnum;
    @JsonProperty("sticker_num")
    private String stickerNum;
    @JsonProperty("applicability")
    private boolean applicability;

}
