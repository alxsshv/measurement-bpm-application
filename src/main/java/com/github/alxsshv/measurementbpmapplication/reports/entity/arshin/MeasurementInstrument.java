package com.github.alxsshv.measurementbpmapplication.reports.entity.arshin;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasurementInstrument {

    private String miTypeNumber; // Идентификатор типа СИ
    private String title;
    private String modification; // Модификация
    private String serialNum; // Заводской номер или серийный номер
    private String owner; // Владелец СИ



    @Override
    public String toString() {
        return "MeasurementInstrument{" +
                "  miType=" + miTypeNumber +
                ", title='" + title + '\'' +
                ", modification='" + modification + '\'' +
                ", serialNum='" + serialNum + '\'' +
                ", owner=" + owner +

                '}';
    }
}
