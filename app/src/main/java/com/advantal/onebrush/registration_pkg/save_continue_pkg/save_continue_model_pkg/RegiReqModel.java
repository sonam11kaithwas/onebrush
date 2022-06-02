package com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg;

import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;

/**
 * Created by Sonam on 02-03-2022 13:10.
 */
public class RegiReqModel {
    private final String deviceIp;
    private  String telefonIsoCode;
    private final String gender;
    private final int allowFirmWareUpdate;
    private final String brushName;
    private final String city;
    private final String deviceToken;
    private final String agreedToTermsAndCond;
    private final int loadedRecord;
    private final String name;
    private final String agreedToPersonalData;
    private final String deviceId;
    private final String password;
    private final String country;
    private final String preferredLanguage;
    private final String streetAndHousenumber;
    private final String dateOfBirth;
    private final String surname;
    private final String telefon;
    private final String agreedToInformation;
    private final String zipcode;
    private final String deviceType;
    private final String allowPushCom;
    private final String firstDentinosticLaunch;
    private final String userType;
    private final String emailAddress;
    private final String salutation;
    private final String firstLaunch;
    private final String recordCreationFunction;
    private final String recordCreationProfile;
    private String recordUpdateFunction;
    private String recordUpdateProfile;
//    recordCreationFunction= android_v_1.5_add
//            recordUpdateFunction=android_v_1.5_update

    public String getTelefonIsoCode() {
        return telefonIsoCode;
    }

    public void setTelefonIsoCode(String telefonIsoCode) {
        this.telefonIsoCode = telefonIsoCode;
    }

    public String getRecordUpdateFunction() {
        return recordUpdateFunction;
    }

    public void setRecordUpdateFunction(String recordUpdateFunction) {
        this.recordUpdateFunction = recordUpdateFunction;
    }

    public String getRecordUpdateProfile() {
        return recordUpdateProfile;
    }

    public void setRecordUpdateProfile(String recordUpdateProfile) {
        this.recordUpdateProfile = recordUpdateProfile;
    }

    public RegiReqModel(String gender, int allowFirmWareUpdate,
                        String brushName, String city,
                        String deviceToken,
                        String agreedToTermsAndCond
            , int loadedRecord, String name, String agreedToPersonalData, String password,
                        String country, String preferredLanguage, String streetAndHousenumber, String dateOfBirth,
                        String surname, String telefon, String agreedToInformation, String zipcode,
                        String allowPushCom, String firstDentinosticLaunch, String emailAddress,
                        String salutation, String firstLaunch, String recordCreationFunction, String recordCreationProfile
    , String telefonIsoCode) {
        this.deviceIp = AppUtility.getIPAddress(true);
        this.telefonIsoCode=telefonIsoCode;
        this.gender = gender;
        this.allowFirmWareUpdate = allowFirmWareUpdate;
        this.brushName = brushName;
        this.city = city;
        this.deviceToken = deviceToken;
        this.agreedToTermsAndCond = agreedToTermsAndCond;
        this.loadedRecord = loadedRecord;
        this.name = name;
        this.agreedToPersonalData = agreedToPersonalData;
        this.deviceId = AppConstant.deviceId;//"B886D96E-4339-482E-84A7-D8891961C22D";//
        this.password = password;
        this.country = country;
        this.preferredLanguage = preferredLanguage;
        this.streetAndHousenumber = streetAndHousenumber;
        this.dateOfBirth = dateOfBirth;
        this.surname = surname;
        this.telefon = telefon;
        this.agreedToInformation = agreedToInformation;
        this.zipcode = zipcode;
        this.deviceType = "Android";
        this.allowPushCom = allowPushCom;
        this.firstDentinosticLaunch = firstDentinosticLaunch;
        this.userType = "D";
        this.emailAddress = emailAddress;
        this.salutation = salutation;
        this.firstLaunch = firstLaunch;
        this.recordCreationFunction = recordCreationFunction;
        this.recordCreationProfile = recordCreationProfile;
    }

    /*{
"uuId":"aba8e551-ee90-430a-bcea-6247b9e783b6",
"userType": "D",
"brushName": "string",
"salutation": "string",
"name": "alok",
"surname": "patel",
"preferredLanguage": "EN",
"gender": "M",
"dateOfBirth": "2022-02-23",             Type : String To Date
"streetAndHousenumber": "string",
 "zipcode": "234234",
 "city": "string",
 "country": "india",
 "telefon": "2322234",
 "allowPushCom": "2022-02-23",    Type : String To Date
"recordUpdateFunction":"string",    **** Here Red Color New parameters ***
"recordUpdateProfile":"string"
}
*/



    public String getDeviceIp() {
        return deviceIp;
    }

    public String getGender() {
        return gender;
    }

    public int getAllowFirmWareUpdate() {
        return allowFirmWareUpdate;
    }

    public String getBrushName() {
        return brushName;
    }

    public String getCity() {
        return city;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getAgreedToTermsAndCond() {
        return agreedToTermsAndCond;
    }

    public int getLoadedRecord() {
        return loadedRecord;
    }

    public String getName() {
        return name;
    }

    public String getAgreedToPersonalData() {
        return agreedToPersonalData;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public String getStreetAndHousenumber() {
        return streetAndHousenumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getAgreedToInformation() {
        return agreedToInformation;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getAllowPushCom() {
        return allowPushCom;
    }

    public String getFirstDentinosticLaunch() {
        return firstDentinosticLaunch;
    }

    public String getUserType() {
        return userType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getFirstLaunch() {
        return firstLaunch;
    }

    public String getRecordCreationFunction() {
        return recordCreationFunction;
    }

    public String getRecordCreationProfile() {
        return recordCreationProfile;
    }


    //git pull origin surbhicode
    //git commit -m "sonam code"
    //git push origin sonamChanges

//    public RegiReqModel(int allowFirmWareUpdate, String allowPushCom, String brushName, String city,
//                        String country, String dateOfBirth, String emailAddress, String firstDentinosticLaunch,
//                        String gender, int loadedRecord, String name, String password,
//                        String preferredLanguage, String salutation, String streetAndHousenumber, String surname,
//                        String telefon, String zipcode
//            , String agreedToTermsAndCond
//            , String agreedToPersonalData, String agreedToInformation) {
//        this.allowFirmWareUpdate = allowFirmWareUpdate;
//        this.agreedToInformation = agreedToInformation;
//        this.agreedToPersonalData = agreedToPersonalData;
//        this.agreedToTermsAndCond = agreedToTermsAndCond;
//        this.allowPushCom = allowPushCom;
//        this.brushName = brushName;
//        this.city = city;
//        this.country = country;
//        this.dateOfBirth = dateOfBirth;
//        this.deviceId = AppConstant.deviceId;
//        this.deviceIp = AppUtility.getIPAddress(true);
//        this.deviceToken = "";
//        this.deviceType = "Android";
//        this.emailAddress = emailAddress;
//        this.firstDentinosticLaunch = firstDentinosticLaunch;
//        this.gender = gender;
//        this.loadedRecord = loadedRecord;
//        this.name = name;
//        this.password = password;
//        this.preferredLanguage = preferredLanguage;
//        this.salutation = salutation;
//        this.streetAndHousenumber = streetAndHousenumber;
//        this.surname = surname;
//        this.telefon = telefon;
//        this.userType = "D";
//        this.zipcode = zipcode;
//    }
}
