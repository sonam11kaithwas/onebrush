package com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_model_pkg;

/**
 * Created by Surbhi joshi on 09-03-2022 18:48.
 */
public class AccountModel {
    private final String uuId;
    private final String oldPassword;
    private final String newPassword;

    public AccountModel(String uuId, String oldPassword, String newPassword) {
        this.uuId = uuId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUuId() {
        return uuId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
