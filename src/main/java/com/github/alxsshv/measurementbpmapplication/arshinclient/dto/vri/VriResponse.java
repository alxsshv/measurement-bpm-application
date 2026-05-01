package com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri;

import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.Response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VriResponse extends Response {
    private VriResult result;
}
