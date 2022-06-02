package com.advantal.onebrush;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam on 21-02-2022 13:05.
 */


@Entity(tableName = "userAccountDetailsModel", indices = {@Index(value = "uuId", unique = true)})
public class UserAccountDetailsModel implements Parcelable {


    @PrimaryKey
    @NonNull
    private String uuId;
    private String custId;
    private String telefonIsoCode;
    private String agreedToInformation;
    private String agreedToPersonalData;
    private String agreedToTermsAndCond;
    private int allowFirmWareUpdate;
    private String allowPushCom;
    private String brushName;
    private String city;
    private String country;
    private String dateOfBirth;
    private String emailAddress;
    private String firstDentinosticLaunch;
    private String firstLaunch;
    private String gender;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private Boolean isVerified;
    private String lastSync;
    private String lastUpdationDate;
    private int loadedRecord;
    private String name;
    private String password;
    private String preferredLanguage;
    private String salutation;
    private String streetAndHousenumber;
    private String surname;
    private String telefon;
    private String userType;
    private String zipcode;
    private String pinApprovalCounter;
    private String creationDate;
    private String userAccId;
    /*local manage*/
    private String lastScreen;
    private String lastAction;
    private String dialoguePhase;
    private String idMethod;
    private String registeredPIN1;
    private String registeredPIN2;
    private String registrationTimePIN;
    private String registrationTimeBiom;
    private String simulatePairingIssue;
    private String memoryPage;
    private String registrationCom;
    private String stateForPin;

    public UserAccountDetailsModel() {
    }


    public UserAccountDetailsModel(@NonNull String uuId,String custId, String agreedToInformation, String agreedToPersonalData,
                                   String agreedToTermsAndCond, int allowFirmWareUpdate, String allowPushCom,
                                   String brushName, String city, String country,
                                   String dateOfBirth, String emailAddress
            , String firstDentinosticLaunch,
                                   String firstLaunch,
                                   String gender, String lastSync,
                                   String lastUpdationDate, int loadedRecord, String name,
                                   String password, String preferredLanguage, String salutation,
                                   String streetAndHousenumber, String surname, String telefon,
                                   String userType, String zipcode, String pinApprovalCounter,
                                   String lastScreen, String lastAction, String dialoguePhase,
                                   String idMethod, String registeredPIN1, String registeredPIN2,
                                   String registrationTimePIN, String registrationTimeBiom, String simulatePairingIssue,String registrationCom
    ,String telefonIsoCode)

    {
        this.uuId = uuId;
        this.telefonIsoCode=telefonIsoCode;
        this.agreedToInformation = agreedToInformation;
        this.agreedToPersonalData = agreedToPersonalData;this.custId=custId;
        this.agreedToTermsAndCond = agreedToTermsAndCond;
        this.allowFirmWareUpdate = allowFirmWareUpdate;
        this.allowPushCom = allowPushCom;
        this.brushName = brushName;
        this.city = city;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.emailAddress = emailAddress;
        this.firstDentinosticLaunch = firstDentinosticLaunch;
        this.firstLaunch = firstLaunch;
        this.gender = gender;
        this.isBlocked = isBlocked;
        this.isDeleted = isDeleted;
        this.isVerified = isVerified;
        this.lastSync = lastSync;
        this.lastUpdationDate = lastUpdationDate;
        this.loadedRecord = loadedRecord;
        this.name = name;
        this.password = password;
        this.preferredLanguage = preferredLanguage;
        this.salutation = salutation;
        this.streetAndHousenumber = streetAndHousenumber;
        this.surname = surname;
        this.telefon = telefon;
        this.userType = userType;
        this.zipcode = zipcode;
        this.pinApprovalCounter = pinApprovalCounter;
        this.lastScreen = lastScreen;
        this.lastAction = lastAction;
        this.dialoguePhase = dialoguePhase;
        this.idMethod = idMethod;
        this.registeredPIN1 = registeredPIN1;
        this.registeredPIN2 = registeredPIN2;
        this.registrationTimePIN = registrationTimePIN;
        this.registrationTimeBiom = registrationTimeBiom;
        this.simulatePairingIssue = simulatePairingIssue;
        this.memoryPage = memoryPage;
        this.creationDate = creationDate;
        this.registrationCom = registrationCom;
    }

    protected UserAccountDetailsModel(Parcel in) {
        uuId = in.readString();
        agreedToInformation = in.readString();
        agreedToPersonalData = in.readString();
        agreedToTermsAndCond = in.readString();
        custId = in.readString();
        allowFirmWareUpdate = in.readInt();
        allowPushCom = in.readString();
        brushName = in.readString();
        city = in.readString();
        country = in.readString();
        dateOfBirth = in.readString();
        emailAddress = in.readString();
        firstDentinosticLaunch = in.readString();
        firstLaunch = in.readString();
        gender = in.readString();
        byte tmpIsBlocked = in.readByte();
        isBlocked = tmpIsBlocked == 0 ? null : tmpIsBlocked == 1;
        byte tmpIsDeleted = in.readByte();
        isDeleted = tmpIsDeleted == 0 ? null : tmpIsDeleted == 1;
        byte tmpIsVerified = in.readByte();
        isVerified = tmpIsVerified == 0 ? null : tmpIsVerified == 1;
        lastSync = in.readString();
        lastUpdationDate = in.readString();
        loadedRecord = in.readInt();
        name = in.readString();
        password = in.readString();
        preferredLanguage = in.readString();
        salutation = in.readString();
        streetAndHousenumber = in.readString();
        surname = in.readString();
        telefon = in.readString();
        userType = in.readString();
        zipcode = in.readString();
        pinApprovalCounter = in.readString();
        creationDate = in.readString();
        userAccId = in.readString();
        telefonIsoCode = in.readString();
        lastScreen = in.readString();
        lastAction = in.readString();
        dialoguePhase = in.readString();
        idMethod = in.readString();
        registeredPIN1 = in.readString();
        registeredPIN2 = in.readString();
        registrationTimePIN = in.readString();
        registrationTimeBiom = in.readString();
        simulatePairingIssue = in.readString();
        memoryPage = in.readString();
        registrationCom = in.readString();
        stateForPin = in.readString();
    }

    public static final Creator<UserAccountDetailsModel> CREATOR = new Creator<UserAccountDetailsModel>() {
        @Override
        public UserAccountDetailsModel createFromParcel(Parcel in) {
            return new UserAccountDetailsModel(in);
        }

        @Override
        public UserAccountDetailsModel[] newArray(int size) {
            return new UserAccountDetailsModel[size];
        }
    };

    public static Creator<UserAccountDetailsModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuId);
        parcel.writeString(agreedToInformation);
        parcel.writeString(agreedToPersonalData);
        parcel.writeString(custId);
        parcel.writeString(agreedToTermsAndCond);
        parcel.writeInt(allowFirmWareUpdate);
        parcel.writeString(allowPushCom);
        parcel.writeString(brushName);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeString(dateOfBirth);
        parcel.writeString(emailAddress);
        parcel.writeString(firstDentinosticLaunch);
        parcel.writeString(firstLaunch);
        parcel.writeString(telefonIsoCode);
        parcel.writeString(gender);
        parcel.writeByte((byte) (isBlocked == null ? 0 : isBlocked ? 1 : 2));
        parcel.writeByte((byte) (isDeleted == null ? 0 : isDeleted ? 1 : 2));
        parcel.writeByte((byte) (isVerified == null ? 0 : isVerified ? 1 : 2));
        parcel.writeString(lastSync);
        parcel.writeString(lastUpdationDate);
        parcel.writeInt(loadedRecord);
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeString(preferredLanguage);
        parcel.writeString(salutation);
        parcel.writeString(streetAndHousenumber);
        parcel.writeString(surname);
        parcel.writeString(telefon);
        parcel.writeString(userType);
        parcel.writeString(zipcode);
        parcel.writeString(pinApprovalCounter);
        parcel.writeString(creationDate);
        parcel.writeString(userAccId);
        parcel.writeString(lastScreen);
        parcel.writeString(lastAction);
        parcel.writeString(dialoguePhase);
        parcel.writeString(idMethod);
        parcel.writeString(registeredPIN1);
        parcel.writeString(registeredPIN2);
        parcel.writeString(registrationTimePIN);
        parcel.writeString(registrationTimeBiom);
        parcel.writeString(simulatePairingIssue);
        parcel.writeString(memoryPage);
        parcel.writeString(registrationCom);
        parcel.writeString(stateForPin);
    }

    @NonNull
    public String getUuId() {
        return uuId;
    }

    public void setUuId(@NonNull String uuId) {
        this.uuId = uuId;
    }

    public String getAgreedToInformation() {
        return agreedToInformation;
    }

    public void setAgreedToInformation(String agreedToInformation) {
        this.agreedToInformation = agreedToInformation;
    }

    public String getTelefonIsoCode() {
        return telefonIsoCode;
    }

    public void setTelefonIsoCode(String telefonIsoCode) {
        this.telefonIsoCode = telefonIsoCode;
    }

    public String getAgreedToPersonalData() {
        return agreedToPersonalData;
    }

    public void setAgreedToPersonalData(String agreedToPersonalData) {
        this.agreedToPersonalData = agreedToPersonalData;
    }

    public String getAgreedToTermsAndCond() {
        return agreedToTermsAndCond;
    }

    public void setAgreedToTermsAndCond(String agreedToTermsAndCond) {
        this.agreedToTermsAndCond = agreedToTermsAndCond;
    }

    public int getAllowFirmWareUpdate() {
        return allowFirmWareUpdate;
    }

    public void setAllowFirmWareUpdate(int allowFirmWareUpdate) {
        this.allowFirmWareUpdate = allowFirmWareUpdate;
    }

    public String getAllowPushCom() {
        return allowPushCom;
    }

    public void setAllowPushCom(String allowPushCom) {
        this.allowPushCom = allowPushCom;
    }

    public String getBrushName() {
        return brushName;
    }

    public void setBrushName(String brushName) {
        this.brushName = brushName;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstDentinosticLaunch() {
        return firstDentinosticLaunch;
    }

    public void setFirstDentinosticLaunch(String firstDentinosticLaunch) {
        this.firstDentinosticLaunch = firstDentinosticLaunch;
    }

    public String getFirstLaunch() {
        return firstLaunch;
    }

    public void setFirstLaunch(String firstLaunch) {
        this.firstLaunch = firstLaunch;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Boolean getVerified() {
        return isVerified;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public String getLastUpdationDate() {
        return lastUpdationDate;
    }

    public void setLastUpdationDate(String lastUpdationDate) {
        this.lastUpdationDate = lastUpdationDate;
    }

    public int getLoadedRecord() {
        return loadedRecord;
    }

    public void setLoadedRecord(int loadedRecord) {
        this.loadedRecord = loadedRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getStreetAndHousenumber() {
        return streetAndHousenumber;
    }

    public void setStreetAndHousenumber(String streetAndHousenumber) {
        this.streetAndHousenumber = streetAndHousenumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPinApprovalCounter() {
        return pinApprovalCounter;
    }

    public void setPinApprovalCounter(String pinApprovalCounter) {
        this.pinApprovalCounter = pinApprovalCounter;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserAccId() {
        return userAccId;
    }

    public void setUserAccId(String userAccId) {
        this.userAccId = userAccId;
    }

    public String getLastScreen() {
        return lastScreen;
    }

    public void setLastScreen(String lastScreen) {
        this.lastScreen = lastScreen;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public String getDialoguePhase() {
        return dialoguePhase;
    }

    public void setDialoguePhase(String dialoguePhase) {
        this.dialoguePhase = dialoguePhase;
    }

    public String getIdMethod() {
        return idMethod;
    }

    public void setIdMethod(String idMethod) {
        this.idMethod = idMethod;
    }

    public String getRegisteredPIN1() {
        return registeredPIN1;
    }

    public void setRegisteredPIN1(String registeredPIN1) {
        this.registeredPIN1 = registeredPIN1;
    }

    public String getRegisteredPIN2() {
        return registeredPIN2;
    }

    public void setRegisteredPIN2(String registeredPIN2) {
        this.registeredPIN2 = registeredPIN2;
    }

    public String getRegistrationTimePIN() {
        return registrationTimePIN;
    }

    public void setRegistrationTimePIN(String registrationTimePIN) {
        this.registrationTimePIN = registrationTimePIN;
    }

    public String getRegistrationTimeBiom() {
        return registrationTimeBiom;
    }

    public void setRegistrationTimeBiom(String registrationTimeBiom) {
        this.registrationTimeBiom = registrationTimeBiom;
    }

    public String getSimulatePairingIssue() {
        return simulatePairingIssue;
    }

    public void setSimulatePairingIssue(String simulatePairingIssue) {
        this.simulatePairingIssue = simulatePairingIssue;
    }

    public String getMemoryPage() {
        return memoryPage;
    }

    public void setMemoryPage(String memoryPage) {
        this.memoryPage = memoryPage;
    }

    public String getRegistrationCom() {
        return registrationCom;
    }

    public void setRegistrationCom(String registrationCom) {
        this.registrationCom = registrationCom;
    }

    public String getStateForPin() {
        return stateForPin;
    }

    public void setStateForPin(String stateForPin) {
        this.stateForPin = stateForPin;
    }
}
