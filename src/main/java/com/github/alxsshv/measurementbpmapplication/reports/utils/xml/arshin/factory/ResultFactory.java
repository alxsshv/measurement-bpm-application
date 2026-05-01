package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.common.config.OrganizationConfig;
import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.BriefProcedure;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Result;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class ResultFactory {

    public static Result createResult(ArshinVerificationRecord vrfRecord, String signCipher){

        Result result = new Result();
        result.setMiInfo(MiInfoFactory.createMiInfo(vrfRecord));
        result.setSignCipher(signCipher);
        result.setMiOwner(buildMiOwner(vrfRecord));
        result.setVrfDate(formatDateToXMLString(vrfRecord.getVerificationDate()));
        result.setValidDate(formatDateToXMLString(vrfRecord.getValidDate()));
        result.setVriType(vrfRecord.getVerificationType());
        result.setCalibration(vrfRecord.isCalibration());
        if (vrfRecord.isApplicable()){
           result.setApplicable(ApplicabilityFactory.createApplicable(vrfRecord));
        } else result.setInapplicable(ApplicabilityFactory.createInapplicable(vrfRecord));
        result.setDocTitle(vrfRecord.getInstruction());
        result.setMetrologist(vrfRecord.getEmployee());
        result.setMeans(MeanFactory.createMeans(vrfRecord));
        result.setConditions(ConditionsFactory.createConditions(vrfRecord));
        if (vrfRecord.isShortVerification()){
            result.setBriefProcedure(new BriefProcedure(vrfRecord.getShortVerificationCharacteristic()));
        }
        return result;
    }

    private String buildMiOwner(ArshinVerificationRecord vrfRecord){
        if (vrfRecord.getMi().getOwner() != null) {
            return vrfRecord.getMi().getOwner();
        } else return "Не указан";
    }

    private String formatDateToXMLString(LocalDate date){
        if (date == null){
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"+03:00";
    }




}
