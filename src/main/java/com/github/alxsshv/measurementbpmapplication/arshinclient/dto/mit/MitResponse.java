package com.github.alxsshv.measurementbpmapplication.arshinclient.dto.mit;


import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.Response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MitResponse extends Response {
    private MitResult result;
}
