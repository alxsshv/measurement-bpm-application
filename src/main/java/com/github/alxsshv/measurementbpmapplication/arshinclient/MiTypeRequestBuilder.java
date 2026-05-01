package com.github.alxsshv.measurementbpmapplication.arshinclient;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MiTypeRequestBuilder {
    private String uriBase;
    private String number;
    private String title;
    private String notation;
    private String manufacturer;


    public MiTypeRequestBuilder() {
    }

    public MiTypeRequestBuilder uri(String uri) {
        this.uriBase = uri;
        return this;
    }

    public MiTypeRequestBuilder number(String number) {
        this.number = number;
        return this;
    }

    public MiTypeRequestBuilder title(String title) {
        this.title = title;
        return this;
    }

    public MiTypeRequestBuilder notation(String notation) {
        this.notation = notation;
        return this;
    }

    public MiTypeRequestBuilder manufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }


    public String build(){
        List<String> parameters = new ArrayList<>();
        if (number != null) {
            parameters.add("number=" + number);
        }
        if (title != null) {
            parameters.add("title=" + title);
        }
        if (notation != null) {
            parameters.add("notation=" + notation);
        }
        if (manufacturer != null) {
            parameters.add("manufacturer=" + manufacturer);
        }

        List<String> parametersWithDelimiter = insertDelimiter(parameters,"&");
        String parametersString = String.join("", parametersWithDelimiter);
        return uriBase + parametersString;
    }

    private List<String> insertDelimiter(List<String> list, String delimiter) {
        return list.stream().flatMap(s -> Stream.of(s, delimiter))
                .limit(list.size() * 2L - 1).toList();
    }



}
