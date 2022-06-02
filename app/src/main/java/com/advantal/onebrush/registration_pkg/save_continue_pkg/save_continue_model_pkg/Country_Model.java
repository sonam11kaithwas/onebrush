package com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg;

/**
 * Created by Surbhi on 2022-04-26 02:36 PM.
 */
public class Country_Model {

    private String id;
    private String name;
    private String isoCode;
    private String countryCode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
