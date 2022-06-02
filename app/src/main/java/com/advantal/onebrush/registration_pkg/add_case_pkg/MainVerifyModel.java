package com.advantal.onebrush.registration_pkg.add_case_pkg;

/**
 * Created by Sonam on 15-04-2022 17:06.
 */
public class MainVerifyModel {
    private String uuId;
//    private String languageCode;

    public MainVerifyModel(String uuId) {//, String languageCode
        this.uuId = uuId;
//        this.languageCode = languageCode;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

//    public String getLanguageCode() {
//        return languageCode;
//    }
//
//    public void setLanguageCode(String languageCode) {
//        this.languageCode = languageCode;
//    }
}
