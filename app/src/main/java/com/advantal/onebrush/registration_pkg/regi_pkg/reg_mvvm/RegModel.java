package com.advantal.onebrush.registration_pkg.regi_pkg.reg_mvvm;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Sonam on 28-02-2022 14:31.
 */
public class RegModel {
    private String email="", password="";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return this.email != null && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private String surname;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String preferredLanguage;
    private String allowFirmWareUpdate;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getAllowFirmWareUpdate() {
        return allowFirmWareUpdate;
    }

    public void setAllowFirmWareUpdate(String allowFirmWareUpdate) {
        this.allowFirmWareUpdate = allowFirmWareUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

