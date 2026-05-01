package com.github.alxsshv.measurementbpmapplication.common.dto.reports.arshin;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasurementInstrumentDto {

    private String miTypeNumber; // Идентификатор типа СИ
    private String title;
    private String modification; // Модификация
    private String serialNum; // Заводской номер или серийный номер
    private String owner; // Владелец СИ



    @Override
    public String toString() {
        return "MeasurementInstrumentDto {" +
                "miType=" + miTypeNumber +
                ", title='" + title + '\'' +
                ", modification='" + modification + '\'' +
                ", serialNum='" + serialNum + '\'' +
                ", owner=" + owner +

                '}';
    }
}
