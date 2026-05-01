package com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.Result;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class VriResult extends Result {
    private int count;
    private int start;
    private int rows;
    private List<VriItem> items = new ArrayList<>();
}
