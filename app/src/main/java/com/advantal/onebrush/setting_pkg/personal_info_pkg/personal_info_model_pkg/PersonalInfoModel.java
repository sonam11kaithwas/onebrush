package com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_model_pkg;

import com.advantal.onebrush.BuildConfig;

/**
 * Created by Surbhi joshi on 03-03-2022 17:57.
 */
public class PersonalInfoModel {
    private final String uuId;
    private final String userType;
    private final String brushName;
    private final String salutation;
    private final String name;
    private final String surname;
    private final String preferredLanguage;
    private final String gender;
    private final String streetAndHousenumber;
    private final String zipcode;
    private final String city;
    private final String country;
    private final String telefon;
    private final String allowPushCom;
    private final String recordUpdateFunction;
    private final String recordUpdateProfile;
    private String dateOfBirth;

    public PersonalInfoModel(String uuId, String userType, String brushName, String salutation,
                             String name, String surname, String preferredLanguage, String gender, String dateOfBirth,
                             String streetAndHousenumber, String zipcode, String city, String country, String telefon,
                             String allowPushCom, String recordUpdateFunction, String recordUpdateProfile) {
        this.uuId = uuId;
        this.userType = userType;
        this.brushName = brushName;
        this.salutation = salutation;
        this.name = name;
        this.surname = surname;
        this.preferredLanguage = preferredLanguage;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.streetAndHousenumber = streetAndHousenumber;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.telefon = telefon;
        this.allowPushCom = allowPushCom;
        this.recordUpdateFunction =  "android_v_" + BuildConfig.VERSION_NAME
                + "_update";
        this.recordUpdateProfile ="";
    }

    public String getUuId() {
        return uuId;
    }

    public String getUserType() {
        return userType;
    }

    public String getBrushName() {
        return brushName;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStreetAndHousenumber() {
        return streetAndHousenumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getAllowPushCom() {
        return allowPushCom;
    }

    public String getRecordUpdateFunction() {
        return recordUpdateFunction;
    }

    public String getRecordUpdateProfile() {
        return recordUpdateProfile;
    }


    //    private final String lastSync;


//    public PersonalInfoModel(String uuId, String name, String surname, String dateOfBirth, String gender, String preferredLanguage, String userType, String brushName, String lastSync, String salutation, String streetAndHousenumber, String zipcode, String city, String country, String telefon, String allowPushCom) {
//        this.uuId = uuId;
//        this.name = name;
//        this.surname = surname;
//        this.dateOfBirth = dateOfBirth;
//        this.gender = gender;
//        this.preferredLanguage = preferredLanguage;
//        this.userType = userType;
//        this.brushName = brushName;
//        this.lastSync = lastSync;
//        this.salutation = salutation;
//        this.streetAndHousenumber = streetAndHousenumber;
//        this.zipcode = zipcode;
//        this.city = city;
//        this.country = country;
//        this.telefon = telefon;
//        this.allowPushCom = allowPushCom;
//    }

   /* public String getUuId() {
        return uuId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public String getUserType() {
        return userType;
    }

    public String getBrushName() {
        return brushName;
    }

//    public String getLastSync() {
//        return lastSync;
//    }

    public String getSalutation() {
        return salutation;
    }

    public String getStreetAndHousenumber() {
        return streetAndHousenumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getAllowPushCom() {
        return allowPushCom;
    }*/
}
