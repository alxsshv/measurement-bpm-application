package com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.factory;

import com.github.alxsshv.measurementbpmapplication.reports.entity.arshin.ArshinVerificationRecord;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Applicable;
import com.github.alxsshv.measurementbpmapplication.reports.utils.xml.arshin.tag.Inapplicable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicabilityFactory {

    public static Applicable createApplicable(ArshinVerificationRecord vrfRecord){
            Applicable applicable = new Applicable();
            String stickerNum = vrfRecord.getStickerNum() != null ? vrfRecord.getStickerNum() : "отсутствует";
            applicable.setStickerNum(stickerNum);
            applicable.setSignPass(vrfRecord.isSignPass());
            applicable.setSignMi(vrfRecord.isSignMi());
            return applicable;
    }
    public static Inapplicable createInapplicable(ArshinVerificationRecord vrfRecord){
        Inapplicable inapplicable = new Inapplicable();
        inapplicable.setReasons(vrfRecord.getInapplicableReason());
        return inapplicable;
    }
}
