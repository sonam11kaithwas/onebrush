package com.advantal.onebrush.api_calls_cntrl_pkg;

/**
 * Created by Sonam on 01-03-2022 16:13.
 */
public class CommonReqModel {
    private String data;
    private String authId;
    private String languageCode;

    public CommonReqModel(String data, String authId) {
        this.data = data;
        this.authId = authId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
}
