package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.fsa.FsaVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.DateStringConverter;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.fsa.tag.MeasurementInstrument;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

/** Фабрика объектов "Средство измерений" */
@UtilityClass
public class MeasurementInstrumentFactory {

    /** Формирование объекта "Средство измерений" на основе записи о поверке */
    public static MeasurementInstrument createInstrument(FsaVerificationRecord vrfRecord){
        MeasurementInstrument instrument = new MeasurementInstrument();
        String[] verificationNumberParts = vrfRecord.getNumberVerification().split("/");
        instrument.setNumberVerification(verificationNumberParts[2]);
        instrument.setTypeMeasuringInstrument(vrfRecord.getTypeMeasurementInstrument());
        instrument.setDateVerification(DateStringConverter.getStringOrNull(vrfRecord.getDateVerification()).substring(0,10));
        setDateEndVerificationIfExist(vrfRecord, instrument);
        setBigDateEndVerificationIfVerificationIsIndefinite(vrfRecord, instrument);
        instrument.setApprovedEmployee(ApprovedEmployeeFactory.createApprovedEmployee(vrfRecord.getEmployee()));
        instrument.setResultVerification(vrfRecord.getResultVerification());
        return instrument;
    }

    /** Установка даты окончания поверки, если дата указана */
    private static void setDateEndVerificationIfExist(FsaVerificationRecord rec, MeasurementInstrument instrument) {
        if (rec.getDateEndVerification() != null) {
            instrument.setDateEndVerification(DateStringConverter.getStringOrNull(rec.getDateEndVerification()).substring(0, 10));
        }
    }

    /** Установка даты окончания поверки в 100 лет, если поверка бессрочная и результат положительный */
    private static void setBigDateEndVerificationIfVerificationIsIndefinite(FsaVerificationRecord rec,
                                                                            MeasurementInstrument instrument) {
        if (rec.getDateEndVerification() == null && rec.getResultVerification() == 1) {
            LocalDate dateEndVerification = rec.getDateVerification().plusYears(100);
            instrument.setDateEndVerification(DateStringConverter.getStringOrNull(dateEndVerification).substring(0, 10));
        }
    }
    }