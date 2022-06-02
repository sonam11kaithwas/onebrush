package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_model_pkg;

import android.content.Context;

import com.advantal.onebrush.login_package.model_pkg.LoginModelClass;

/**
 * Created by Sonam on 17-02-2022 18:12.
 */
public class PinGenReqModel {
    private Context context;
    private String registeredPIN1;
    private String registeredPIN2;

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




}
