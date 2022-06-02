package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam on 21-03-2022 17:08.
 */
@Entity(tableName = "addPatientModel", indices = {@Index(value = "patientId", unique = true)})
public class AddPatientModel {
    private final String recordCreationFunction;
    private final String recordCreationProfile;
    @PrimaryKey
    private int patientId;
    private String salutation;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String streetAndHousenumber;
    private String zipcode;
    private String city;
    private String country;
    private String telefon;
    private String uuId;
    private String custIdFk;
    private String creationDate;
    private String telefonIsoCode;
    @Ignore
    private boolean checked;

    public AddPatientModel(int patientId, String salutation, String name, String surname, String dateOfBirth, String streetAndHousenumber,
                           String zipcode, String city, String country, String telefon, String uuId, String custIdFk,
                           String telefonIsoCode, String recordCreationFunction

            , String recordCreationProfile) {
        this.salutation = salutation;
        this.patientId = patientId;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.streetAndHousenumber = streetAndHousenumber;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.telefon = telefon;
        this.uuId = uuId;
        this.telefonIsoCode = telefonIsoCode;
        this.recordCreationFunction = recordCreationFunction;
        this.recordCreationProfile = recordCreationProfile;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getCustIdFk() {
        return custIdFk;
    }

    public void setCustIdFk(String custIdFk) {
        this.custIdFk = custIdFk;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTelefonIsoCode() {
        return telefonIsoCode;
    }

    public void setTelefonIsoCode(String telefonIsoCode) {
        this.telefonIsoCode = telefonIsoCode;
    }

    public String getRecordCreationFunction() {
        return recordCreationFunction;
    }

    public String getRecordCreationProfile() {
        return recordCreationProfile;
    }
}
