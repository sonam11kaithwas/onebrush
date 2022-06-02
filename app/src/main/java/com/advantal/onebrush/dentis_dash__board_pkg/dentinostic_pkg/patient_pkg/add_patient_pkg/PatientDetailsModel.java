package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam on 31-03-2022 19:22.
 */
public class PatientDetailsModel {

    private Integer custId;
    private String uuId;
    private String userType;
    private String brushName;
    private String lastSync;
    private String emailaddress;
    private String password;
    private String salutation;
    private String name;
    private String surname;
    private String preferredLanguage;
    private String gender;
    private Integer allowFirmWareUpdate;
    private String dateOfBirth;
    private String streetAndHousenumber;
    private String zipcode;
    private String city;
    private String country;
    private String telefon;
    private String agreedToTermsAndCond;
    private String agreedToPersonalData;
    private String agreedToInformation;
    private String allowPushCom;
    private Integer loadedRecord;
    private String firstLaunch;
    private String firstDentinosticLaunch;
    private String creationDate;
    private String recordCreationFunction;
    private String recordCreationProfile;
    private Boolean isVerified;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private final List<AddPatientModel> patientList = new ArrayList<>();

    public PatientDetailsModel() {
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBrushName() {
        return brushName;
    }

    public void setBrushName(String brushName) {
        this.brushName = brushName;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAllowFirmWareUpdate() {
        return allowFirmWareUpdate;
    }

    public void setAllowFirmWareUpdate(Integer allowFirmWareUpdate) {
        this.allowFirmWareUpdate = allowFirmWareUpdate;
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

    public void setStreetAndHousenumber(String streetAndHousenumber) {
        this.streetAndHousenumber = streetAndHousenumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAgreedToTermsAndCond() {
        return agreedToTermsAndCond;
    }

    public void setAgreedToTermsAndCond(String agreedToTermsAndCond) {
        this.agreedToTermsAndCond = agreedToTermsAndCond;
    }

    public String getAgreedToPersonalData() {
        return agreedToPersonalData;
    }

    public void setAgreedToPersonalData(String agreedToPersonalData) {
        this.agreedToPersonalData = agreedToPersonalData;
    }

    public String getAgreedToInformation() {
        return agreedToInformation;
    }

    public void setAgreedToInformation(String agreedToInformation) {
        this.agreedToInformation = agreedToInformation;
    }

    public String getAllowPushCom() {
        return allowPushCom;
    }

    public void setAllowPushCom(String allowPushCom) {
        this.allowPushCom = allowPushCom;
    }

    public Integer getLoadedRecord() {
        return loadedRecord;
    }

    public void setLoadedRecord(Integer loadedRecord) {
        this.loadedRecord = loadedRecord;
    }

    public String getFirstLaunch() {
        return firstLaunch;
    }

    public void setFirstLaunch(String firstLaunch) {
        this.firstLaunch = firstLaunch;
    }

    public String getFirstDentinosticLaunch() {
        return firstDentinosticLaunch;
    }

    public void setFirstDentinosticLaunch(String firstDentinosticLaunch) {
        this.firstDentinosticLaunch = firstDentinosticLaunch;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRecordCreationFunction() {
        return recordCreationFunction;
    }

    public void setRecordCreationFunction(String recordCreationFunction) {
        this.recordCreationFunction = recordCreationFunction;
    }

    public String getRecordCreationProfile() {
        return recordCreationProfile;
    }

    public void setRecordCreationProfile(String recordCreationProfile) {
        this.recordCreationProfile = recordCreationProfile;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<AddPatientModel> getPatientList() {
        return patientList;
    }
}
