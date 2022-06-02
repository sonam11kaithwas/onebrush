package com.advantal.onebrush.login_package.model_pkg;

import android.text.TextUtils;
import android.util.Patterns;

public class LoginModelClass {
    private String emailAddress = "";
    private String password = "";
    private String deviceType = "android";
    private String deviceId = "";
    private String deviceIp = "";
    private String deviceToken = "";
//{"deviceId": "FB106ECB-9C64-4B81-BC7B-9201DD87A4CE", "deviceToken":
// "1234567890", "deviceType": "iphone", "deviceIp": "", "emailAddress": "moinuddin@advantal.net", "password": "Adv@12345"}


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
