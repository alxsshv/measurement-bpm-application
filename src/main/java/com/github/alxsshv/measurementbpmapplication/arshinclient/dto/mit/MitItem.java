package com.github.alxsshv.measurementbpmapplication.arshinclient.dto.mit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MitItem {
    @JsonProperty("mit_id")
    private String mitId;
    @JsonProperty("number")
    private String number;
    @JsonProperty("title")
    private String title;
    @JsonProperty("notation")
    private String notation;
    @JsonProperty("manufacturer")
    private String manufacturer;
}
