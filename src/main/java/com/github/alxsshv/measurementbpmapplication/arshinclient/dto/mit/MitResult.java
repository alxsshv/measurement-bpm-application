package com.github.alxsshv.measurementbpmapplication.arshinclient.dto.mit;

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
public class MitResult extends Result {
    private int count;
    private int start;
    private int rows;
    private List<MitItem> items = new ArrayList<>();
}
