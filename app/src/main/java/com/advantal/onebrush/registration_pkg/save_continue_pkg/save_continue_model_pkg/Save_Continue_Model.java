package com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg;

import android.content.Context;

/**
 * Created by Surbhi joshi on 24-02-2022 18:47.
 */
public class Save_Continue_Model {
    private Context context;
    private String street = "";
    private int zipcode;
    private String city = "";
    private String country = "";
    private long phone;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
