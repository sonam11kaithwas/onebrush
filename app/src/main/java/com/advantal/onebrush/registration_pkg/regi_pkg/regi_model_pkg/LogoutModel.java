package com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg;

/**
 * Created by Surbhi joshi on 09-03-2022 10:31.
 */
public class LogoutModel {
    private final String uuId;
    private String deviceId = "";

    //{\"uuId\": \"235ea6c5-cd8a-47b2-b508-9720061f22ec\", \"deviceId\": \"D47DEC06-5BD2-4416-A7CB-D2FB1241C747\"}
    public LogoutModel(String uuid, String deviceId) {
        this.uuId = uuid;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUuid() {
        return uuId;
    }
}
